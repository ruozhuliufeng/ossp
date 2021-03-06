package cn.aixuxi.ossp.uaa.granter;

import cn.aixuxi.ossp.auth.client.token.MobileAuthenticationToken;
import cn.aixuxi.ossp.auth.client.token.OpenIdAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * OpenId授权模式
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 11:17
 **/
public class OpenIdGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "openId";

    private final AuthenticationManager authenticationManager;

    public OpenIdGranter(AuthorizationServerTokenServices tokenServices,
                            ClientDetailsService clientDetailsService,
                            OAuth2RequestFactory requestFactory,
                            AuthenticationManager authenticationManager) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String,String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String openId = parameters.get("openId");
        Authentication userAuth = new OpenIdAuthenticationToken(openId);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        userAuth = authenticationManager.authenticate(userAuth);
        if (userAuth == null || !userAuth.isAuthenticated()){
            throw new InvalidGrantException("无法认证openId："+openId);
        }
        OAuth2Request storeOAuth2Request = getRequestFactory().createOAuth2Request(client,tokenRequest);
        return new OAuth2Authentication(storeOAuth2Request,userAuth);
    }
}
