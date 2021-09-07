package cn.aixuxi.ossp.uaa.granter;

import cn.aixuxi.ossp.auth.client.token.MobileAuthenticationToken;
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
 * 手机号密码授权模式
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 11:11
 **/
public class MobilePwdGranter extends AbstractTokenGranter {
    private static final String GRANT_TYPE = "mobile_password";
    private final AuthenticationManager authenticationManager;

    public MobilePwdGranter(AuthenticationManager authenticationManager,
                            AuthorizationServerTokenServices tokenServices,
                            ClientDetailsService clientDetailsService,
                            OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String,String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String mobile = parameters.get("mobile");
        String password = parameters.get("password");
        parameters.remove("password");
        Authentication userAuth = new MobileAuthenticationToken(mobile,password);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        userAuth = authenticationManager.authenticate(userAuth);
        if (userAuth == null || !userAuth.isAuthenticated()){
            throw new InvalidGrantException("无法认证手机号："+mobile);
        }
        OAuth2Request storeOAuth2Request = getRequestFactory().createOAuth2Request(client,tokenRequest);
        return new OAuth2Authentication(storeOAuth2Request,userAuth);
    }
}
