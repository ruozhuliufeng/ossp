package cn.aixuxi.ossp.uaa.password;

import cn.aixuxi.ossp.uaa.service.impl.UserDetailServiceFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 扩展用户密码provider
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-22 16:13
 **/
public class PasswordAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private UserDetailServiceFactory userDetailServiceFactory;
    private static final String USER_NOT_FOUND_PASSROD = "用户未找到密码！";
    private PasswordEncoder passwordEncoder;

    private volatile String userNotFoundEncodedPassword;

    private UserDetailsPasswordService userDetailsPasswordService;

    public PasswordAuthenticationProvider() {
        setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }



    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        return null;
    }

    private void setPasswordEncoder(PasswordEncoder delegatingPasswordEncoder) {
    }
}
