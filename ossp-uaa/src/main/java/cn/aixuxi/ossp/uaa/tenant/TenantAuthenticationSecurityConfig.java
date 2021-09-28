package cn.aixuxi.ossp.uaa.tenant;

import cn.aixuxi.ossp.uaa.service.impl.UserDetailServiceFactory;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 16:59
 **/
@Component
public class TenantAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final UserDetailServiceFactory userDetailServiceFactory;
    private final PasswordEncoder passwordEncoder;

    public TenantAuthenticationSecurityConfig(UserDetailServiceFactory userDetailServiceFactory,
                                              PasswordEncoder passwordEncoder){
        this.userDetailServiceFactory = userDetailServiceFactory;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        TenantAuthenticationProvider provider = new TenantAuthenticationProvider();
        provider.setUserDetailServiceFactory(userDetailServiceFactory);
        provider.setPasswordEncoder(passwordEncoder);
        http.authenticationProvider(provider);
    }
}
