package cn.aixuxi.ossp.gateway.auth;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import reactor.core.publisher.Mono;

/**
 * 自定义认证管理
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-10 11:02
 **/
public class CustomAuthenticationManager implements ReactiveAuthenticationManager {

    private TokenStore tokenStore;

    public CustomAuthenticationManager(TokenStore tokenStore){
        this.tokenStore = tokenStore;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .filter(a->a instanceof BearerTokenAuthenticationToken)
                .cast(BearerTokenAuthenticationToken.class)
                .map(BearerTokenAuthenticationToken::getToken)
                .flatMap(accessTokenValue ->{
                    OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
                    if (accessToken == null){
                        return Mono.error(new InvalidTokenException("AccessToken无效："+accessTokenValue));
                    }else if (accessToken.isExpired()){
                        tokenStore.removeAccessToken(accessToken);
                        return Mono.error(new InvalidTokenException("AccessToken失效："+accessTokenValue));
                    }
                    OAuth2Authentication result = tokenStore.readAuthentication(accessToken);
                    if (result == null){
                        return Mono.error(new InvalidTokenException("AccessToken无效："+accessTokenValue));
                    }
                    return Mono.just(result);
                })
                .cast(Authentication.class);
    }
}
