package cn.aixuxi.ossp.uaa.config;

import cn.aixuxi.ossp.auth.client.token.CustomWebAuthenticationDetails;
import cn.aixuxi.ossp.common.config.DefaultPasswordConfig;
import cn.aixuxi.ossp.common.constant.SecurityConstants;
import cn.aixuxi.ossp.common.properties.TenantProperties;
import cn.aixuxi.ossp.uaa.filter.LoginProcessSetTenantFilter;
import cn.aixuxi.ossp.uaa.handler.OauthLogoutSuccessHandler;
import cn.aixuxi.ossp.uaa.mobile.MobileAuthenticationSecurityConfig;
import cn.aixuxi.ossp.uaa.openid.OpenIdAuthenticationSecurityConfig;
import cn.aixuxi.ossp.uaa.password.PasswordAuthenticationProvider;
import cn.aixuxi.ossp.uaa.service.impl.UserDetailServiceFactory;
import cn.aixuxi.ossp.uaa.tenant.TenantAuthenticationSecurityConfig;
import cn.aixuxi.ossp.uaa.tenant.TenantUsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Spring Security????????????WebSecurityConfigurerAdapter?????????oauth??????????????????
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 14:15
 **/
@Configuration
@Import(DefaultPasswordConfig.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired(required = false)
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Resource
    private UserDetailServiceFactory userDetailServiceFactory;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Resource
    private LogoutHandler logoutHandler;
    @Resource
    private LogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;
    @Autowired
    private MobileAuthenticationSecurityConfig mobileAuthenticationSecurityConfig;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TenantAuthenticationSecurityConfig tenantAuthenticationSecurityConfig;
    @Autowired
    private TenantProperties tenantProperties;
    @Autowired
    private AuthenticationDetailsSource<HttpServletRequest, CustomWebAuthenticationDetails> authenticationDetailsSource;

    /**
     * ?????????????????????????????????????????????SpringBoot?????????????????????AuthenticationManger???
     * ???????????????????????????
     * @return ??????????????????
     * @throws Exception ??????
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Bean
    public TenantUsernamePasswordAuthenticationFilter tenantUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager){
        TenantUsernamePasswordAuthenticationFilter filter = new TenantUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setFilterProcessesUrl(SecurityConstants.OAUTH_LOGIN_PRO_URL);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(SecurityConstants.LOGIN_FAILURE_PAGE));
        filter.setAuthenticationDetailsSource(authenticationDetailsSource);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                // ?????????????????????basic??????
                .permitAll()
                .and()
                .logout()
                .logoutUrl(SecurityConstants.LOGOUT_URL)
                .logoutSuccessHandler(logoutSuccessHandler)
                .addLogoutHandler(logoutHandler)
                .clearAuthentication(true)
                .and()
                .apply(openIdAuthenticationSecurityConfig)
                .and()
                .apply(mobileAuthenticationSecurityConfig)
                .and()
                .addFilterBefore(new LoginProcessSetTenantFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                // ????????????????????????iframe?????????
                .headers().frameOptions().disable().cacheControl();
        if(tenantProperties.getEnable()){
            // ???????????????????????????????????????????????????
            http.formLogin()
                    .loginPage(SecurityConstants.LOGIN_PAGE)
                    .and()
                    .addFilterAt(tenantUsernamePasswordAuthenticationFilter(authenticationManager),UsernamePasswordAuthenticationFilter.class)
                    .apply(tenantAuthenticationSecurityConfig);
        }else {
            http.formLogin()
                    .loginPage(SecurityConstants.LOGIN_PAGE)
                    .loginProcessingUrl(SecurityConstants.OAUTH_LOGIN_PRO_URL)
                    .successHandler(authenticationSuccessHandler)
                    .authenticationDetailsSource(authenticationDetailsSource);

        }
        // ??????????????????????????????session???????????????????????????
        if (authenticationEntryPoint != null){
            http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }else {
            // ????????????????????????????????????session???????????????????????????????????????oauth2?????????
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        }
    }

    /**
     * ??????????????????
     * @param auth ??????????????????
     * @throws Exception ??????
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordAuthenticationProvider provider = new PasswordAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailServiceFactory(userDetailServiceFactory);
        auth.authenticationProvider(provider);
    }
/*    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }*/

}
