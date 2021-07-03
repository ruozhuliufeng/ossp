package cn.aixuxi.ossp.uaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 类描述 Token的相关配置
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021/5/17 22:46
 */
@Configuration
public class TokenConfig {

    private static final String SIGNING_KEY = "aixuxi.cn";

    @Bean
    public TokenStore tokenStore(){
        // JWT令牌存储方案
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 对称密钥，资源服务器使用此密钥进行验证
        converter.setSigningKey(SIGNING_KEY);
        return converter;
    }
}
