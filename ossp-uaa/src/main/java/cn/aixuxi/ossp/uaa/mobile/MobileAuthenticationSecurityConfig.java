package cn.aixuxi.ossp.uaa.mobile;

import cn.aixuxi.ossp.uaa.service.OsspUserDetailsService;
import cn.aixuxi.ossp.uaa.service.impl.UserDetailServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * mobile的相关处理配置
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 17:26
 **/
@Component
public class MobileAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Resource
    private UserDetailServiceFactory userDetailServiceFactory;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        MobileAuthenticationProvider provider = new MobileAuthenticationProvider();
        provider.setUserDetailServiceFactory(userDetailServiceFactory);
        provider.setPasswordEncoder(passwordEncoder);
        http.authenticationProvider(provider);
    }
}
