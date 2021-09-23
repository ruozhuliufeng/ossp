package cn.aixuxi.ossp.uaa.openid;

import cn.aixuxi.ossp.uaa.service.impl.UserDetailServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

/**
 * OpenId的相关处理配置
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 17:19
 **/
@Component
public class OpenIdAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private UserDetailServiceFactory userDetailServiceFactory;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        OpenIdAuthenticationProvider provider = new OpenIdAuthenticationProvider();
        provider.setUserDetailServiceFactory(userDetailServiceFactory);
        http.authenticationProvider(provider);
    }
}
