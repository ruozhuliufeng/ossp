package cn.aixuxi.ossp.auth.client.store;

import cn.aixuxi.ossp.auth.client.properties.SecurityPropertis;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 优化自Spring Security的{@link org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore}
 * 1. 支持redis所有集群模式，包括cluster模式
 * 2. 使用pipeline减少连接次数，提升性能
 * 3. 自动续签token(可配置是否开启)
 * @author ruozhuliufeng
 * @date 2021-08-07
 */
public class CustomRedisTokenStore implements TokenStore {
    private static final String ACCESS = "access:";
    private static final String AUTH_TO_ACCESS = "auth_to_access:";
    private static final String REFRESH_AUTH = "refresh_auth:";
    private static final String ACCESS_TO_REFRESH = "access_to_refresh:";
    private static final String REFRESH = "refresh:";
    private static final String REFRESH_TO_ACCESS = "refresh_to_access:";

    private static final boolean springDataRedis_2_0 = ClassUtils.isPresent(
            "org.springframework.data.redis.connection.RedisStandaloneConfiguration",
            RedisTokenStore.class.getClassLoader());

    private final RedisConnectionFactory connectionFactory;
    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
    private RedisTokenStoreSerializationStrategy storeSerializationStrategy = new JdkSerializationStrategy();

    private String prefix = "";
    private Method redisConnetciontSet_2_0;

    private SecurityPropertis securityPropertis;

    private RedisSerializer<Object> redisSerializer;

    public CustomRedisTokenStore(RedisConnectionFactory connectionFactory,SecurityPropertis securityPropertis,
                                 RedisSerializer<Object> redisSerializer){
        this.connectionFactory = connectionFactory;
        this.securityPropertis = securityPropertis;
        this.redisSerializer = redisSerializer;
        if (springDataRedis_2_0){
            this.loadRedisConnectionMethods_2_0();
        }
    }

    public void setAuthenticationKeyGenerato(AuthenticationKeyGenerator authenticationKeyGenerator) {
        this.authenticationKeyGenerator = authenticationKeyGenerator;
    }

    public void setStoreSerializationStrategy(RedisTokenStoreSerializationStrategy storeSerializationStrategy) {
        this.storeSerializationStrategy = storeSerializationStrategy;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void loadRedisConnectionMethods_2_0(){
        // 在类型clazz上，查询name方法，参数类型列表为paramTypes
        this.redisConnetciontSet_2_0 = ReflectionUtils.findMethod(
                RedisConnection.class,"set",byte[].class,byte[].class);
    }

    /**
     * 获取Redis连接
     * @return Redis连接
     */
    private RedisConnection getConnection(){
        return connectionFactory.getConnection();
    }

    /**
     * 参数序列化
     * @param object 待序列化的参数
     * @return 序列化后的数据
     */
    private byte[] serialize(Object object){
        return storeSerializationStrategy.serialize(object);
    }
    /**
     * 参数序列化
     * @param object 待序列化的参数
     * @return 序列化后的数据
     */
    private byte[] serializeKey(String object){
        return serialize(prefix+object);
    }

    /**
     * 反序列化访问令牌
     * @param bytes 令牌
     * @return 令牌反序列化后
     */
    private OAuth2AccessToken deserializeAccessToken(byte[] bytes){
        return storeSerializationStrategy.deserialize(bytes,OAuth2AccessToken.class);
    }

    /**
     * 反序列化身份验证
     * @param bytes 身份验证
     * @return 身份验证反序列化后
     */
    private OAuth2Authentication deserializeAuthentication(byte[] bytes){
        return storeSerializationStrategy.deserialize(bytes,OAuth2Authentication.class);
    }

    /**
     * 反序列化刷新令牌
     * @param bytes 刷新令牌
     * @return 反序列化
     */
    private OAuth2RefreshToken deserializeRefreshToken(byte[] bytes){
        return storeSerializationStrategy.deserialize(bytes,OAuth2RefreshToken.class);
    }

    /**
     * 反序列化客户端
     * @param bytes 客户端
     * @return 反序列化
     */
    private ClientDetails deserializeClientDetails(byte[] bytes){
        return (ClientDetails) redisSerializer.deserialize(bytes);
    }

    private byte[] serialize(String string){
        return storeSerializationStrategy.serialize(string);
    }

    private String deserializeString(byte[] bytes){
        return storeSerializationStrategy.deserializeString(bytes);
    }


    /**
     * 检索针对提供的身份验证密钥存储的访问令牌
     * @param oAuth2Authentication 身份认证信息
     * @return 令牌
     */
    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication oAuth2Authentication) {
        // 身份验证，并返回标识身份验证的唯一密钥
        String key = authenticationKeyGenerator.extractKey(oAuth2Authentication);
        byte[] serializeKey = serializeKey(AUTH_TO_ACCESS+key);
        byte[] bytes;
        // Java 7 build 105 版本开始，Java 7 的编译器和运行环境支持新的 try-with-resources 语句，称为 ARM 块(Automatic Resource Management) ，自动资源管理。
        //带有resources的try语句声明一个或多个resources。resources是在程序结束后必须关闭的对象。try-with-resources语句确保在语句末尾关闭每个resources。
        //任何实现java.lang.AutoCloseable,包括实现了java.io.Closeable的类，都可以作为resources使用。
        try (RedisConnection conn = getConnection()) {
            bytes = conn.get(serializeKey);
        }
        OAuth2AccessToken accessToken = deserializeAccessToken(bytes);
        if (accessToken != null){
            OAuth2Authentication storeAuthentication = readAuthentication(accessToken.getValue());
            // 判断令牌是否为空或身份验证是否符合
            if ((storeAuthentication == null || !key.equals(authenticationKeyGenerator.extractKey(storeAuthentication)))){
                // 存储访问令牌
                // 保持身份一致，以免身份验证后有所更改
                storeAccessToken(accessToken,oAuth2Authentication);
            }
        }
        return accessToken;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken oAuth2AccessToken) {
        return null;
    }

    @Override
    public OAuth2Authentication readAuthentication(String s) {
        return null;
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {

    }

    @Override
    public OAuth2AccessToken readAccessToken(String s) {
        return null;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken oAuth2AccessToken) {

    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken, OAuth2Authentication oAuth2Authentication) {

    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String s) {
        return null;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        return null;
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {

    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {

    }



    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String s, String s1) {
        return null;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String s) {
        return null;
    }
}
