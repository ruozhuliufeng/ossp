package cn.aixuxi.ossp.auth.client.store;

import cn.aixuxi.ossp.auth.client.converter.CustomUserAuthenticationConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import java.security.KeyPair;
import java.util.Locale;

/**
 * 认证服务器使用JWT RSA非对称加密令牌
 */
@Configuration
@ConditionalOnProperty(prefix = "ossp.oauth2.token.store",name = "type",havingValue = "authJwt")
public class AuthJwtTokenStore {

    @Resource
    private KeyProperties keyProperties;

    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter converter){
        return new JwtTokenStore(converter);
    }

    @Bean
    @Order(2)
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(
                keyProperties.getKeyStore().getLocation(),
                keyProperties.getKeyStore().getSecret().toCharArray())
                .getKeyPair(keyProperties.getKeyStore().getAlias());
        converter.setKeyPair(keyPair);
        DefaultAccessTokenConverter tokenConverter = (DefaultAccessTokenConverter) converter.getAccessTokenConverter();
        tokenConverter.setUserTokenConverter(new CustomUserAuthenticationConverter());
        return converter;
    }
}
