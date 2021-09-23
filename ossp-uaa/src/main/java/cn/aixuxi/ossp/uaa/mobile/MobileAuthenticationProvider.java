package cn.aixuxi.ossp.uaa.mobile;

import cn.aixuxi.ossp.auth.client.token.MobileAuthenticationToken;
import cn.aixuxi.ossp.uaa.service.OsspUserDetailsService;
import cn.aixuxi.ossp.uaa.service.impl.UserDetailServiceFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 17:25
 **/
@Setter
@Getter
public class MobileAuthenticationProvider implements AuthenticationProvider {

    private UserDetailServiceFactory userDetailServiceFactory;
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MobileAuthenticationToken authenticationToken = (MobileAuthenticationToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();
        String password = (String) authenticationToken.getCredentials();
        UserDetails user = userDetailServiceFactory.getService(authentication).loadUserByMobile(mobile);
        if (user == null){
            throw new InternalAuthenticationServiceException("手机号或密码错误！");
        }
        if (!passwordEncoder.matches(password,user.getPassword())){
            throw new InternalAuthenticationServiceException("手机号或密码错误！");
        }
        MobileAuthenticationToken authentionResult = new MobileAuthenticationToken(user,password,user.getAuthorities());
        authentionResult.setDetails(authenticationToken.getDetails());
        return authentionResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
