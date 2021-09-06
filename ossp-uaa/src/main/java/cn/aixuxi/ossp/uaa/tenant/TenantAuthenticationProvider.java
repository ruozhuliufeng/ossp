package cn.aixuxi.ossp.uaa.tenant;

import cn.aixuxi.ossp.auth.client.token.TenantUsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 增加租户id，解决不同租户单点登录时角色变化
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 17:03
 **/
public class TenantAuthenticationProvider extends DaoAuthenticationProvider {
    @Override
    protected Authentication createSuccessAuthentication(Object principal,
                                                         Authentication authentication,
                                                         UserDetails user) {
        TenantUsernamePasswordAuthenticationToken authenticationToken = (TenantUsernamePasswordAuthenticationToken) authentication;
        TenantUsernamePasswordAuthenticationToken result = new TenantUsernamePasswordAuthenticationToken(
                principal,authentication.getCredentials(),user.getAuthorities(),((TenantUsernamePasswordAuthenticationToken) authentication).getClientId());
        result.setDetails(authenticationToken.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TenantUsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
