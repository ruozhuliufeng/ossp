package cn.aixuxi.ossp.uaa.service.impl;

import cn.aixuxi.ossp.redis.template.RedisRepository;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * JdbcAuthorizationCodeServices替换
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 9:12
 **/
@Service
public class RedisAuthorizationCodeService extends RandomValueAuthorizationCodeServices {

    private final RedisRepository redisRepository;
    private final RedisSerializer<Object> valueSerializer;

    public RedisAuthorizationCodeService(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
        this.valueSerializer = RedisSerializer.java();
    }

    /**
     * 替换jdbcAuthorizationCodeServices的存储策略，将存储code到redis,并设施过期时间，10分钟
     *
     * @param code           授权码
     * @param authentication 认证身份信息
     */
    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        redisRepository.setExpire(redisKey(code), authentication, 10, TimeUnit.MINUTES, valueSerializer);

    }

    @Override
    protected OAuth2Authentication remove(final String code) {
        String codeKey = redisKey(code);
        OAuth2Authentication token = (OAuth2Authentication) redisRepository.get(codeKey,valueSerializer);
        redisRepository.del(codeKey);
        return token;
    }

    /**
     * Redis中code key 的前缀
     * @param code 授权码
     * @return String
     */
    private String redisKey(String code) {
        return "oauth:code:" + code;
    }
}
