package cn.aixuxi.ossp.uaa.openid;

import cn.aixuxi.ossp.auth.client.token.OpenIdAuthenticationToken;
import cn.aixuxi.ossp.uaa.service.impl.UserDetailServiceFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.security.SocialUserDetailsService;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 17:19
 **/
@Getter
@Setter
public class OpenIdAuthenticationProvider implements AuthenticationProvider {

    private UserDetailServiceFactory userDetailServiceFactory;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OpenIdAuthenticationToken authenticationToken = (OpenIdAuthenticationToken) authentication;
        String openId = (String) authenticationToken.getPrincipal();
        UserDetails user = userDetailServiceFactory.getService(authentication).loadUserByUserId(openId);
        if (user == null){
            throw new InternalAuthenticationServiceException("OpenId错误");
        }
        OpenIdAuthenticationToken authenticationResult = new OpenIdAuthenticationToken(user,user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OpenIdAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
