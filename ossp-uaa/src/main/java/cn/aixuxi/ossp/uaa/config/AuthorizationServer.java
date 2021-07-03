package cn.aixuxi.ossp.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * 类描述 授权服务配置
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021/5/17 23:08
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer  extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * 功能描述: 将客户信息存储到数据库
     * @param dataSource 数据库地址
     * @author : ruozhuliufeng
     * @date : 2021/5/17 23:11
     */
    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource){
        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService)clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

    /**
     * 功能描述: 客户端详情服务
     * @param clients 客户端
     * @author : ruozhuliufeng
     * @date : 2021/5/17 23:13
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    public AuthorizationServerTokenServices tokenServices(){
        DefaultTokenServices services = new DefaultTokenServices();
        // 客户端详情服务
        services.setClientDetailsService(clientDetailsService);
        // 支持刷新令牌
        services.setSupportRefreshToken(true);
        // 令牌存储策略
        services.setTokenStore(tokenStore);
        // 令牌增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        services.setTokenEnhancer(tokenEnhancerChain);
        // 令牌默认有效期2小时
        services.setAccessTokenValiditySeconds(7200);
        // 刷新令牌默认有效期3天
        services.setRefreshTokenValiditySeconds(259200);
        return services;
    }

    /**
     * 功能描述: 授权码服务，设置授权码的存取
     * @param dataSource 1
     * @return : org.springframework.security.oauth2.provider.code.AuthorizationCodeServices
     * @author : ruozhuliufeng
     * @date : 2021/5/17 23:19
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        // 设置授权码模式的授权码是如何存取
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // /oauth/token_key是公开
                .tokenKeyAccess("permitAll()")
                // /oauth/check_token是公开
                .checkTokenAccess("permitAll()")
                // 表单认证(申请令牌)
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)// 认证管理器
                .authorizationCodeServices(authorizationCodeServices)// 授权码服务
                .tokenServices(tokenServices())// 令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);// 允许请求方式
    }
}
