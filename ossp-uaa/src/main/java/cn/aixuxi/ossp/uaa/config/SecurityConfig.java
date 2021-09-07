package cn.aixuxi.ossp.uaa.config;

import cn.aixuxi.ossp.common.config.DefaultPasswordConfig;
import cn.aixuxi.ossp.common.constant.SecurityConstants;
import cn.aixuxi.ossp.common.properties.TenantProperties;
import cn.aixuxi.ossp.uaa.filter.LoginProcessSetTenantFilter;
import cn.aixuxi.ossp.uaa.handler.OauthLogoutSuccessHandler;
import cn.aixuxi.ossp.uaa.mobile.MobileAuthenticationSecurityConfig;
import cn.aixuxi.ossp.uaa.openid.OpenIdAuthenticationSecurityConfig;
import cn.aixuxi.ossp.uaa.tenant.TenantAuthenticationSecurityConfig;
import cn.aixuxi.ossp.uaa.tenant.TenantUsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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

import javax.annotation.Resource;

/**
 * Spring Security配置，在WebSecurityConfigurerAdapter不拦截oauth要开放的资源
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
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Resource
    private LogoutHandler oauthLogoutHandler;
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

    /**
     * 这一步的配置是必不可少的，否则SpringBoot会自动配置一个AuthenticationManger，
     * 覆盖掉内存中的用户
     * @return 认证管理对象
     * @throws Exception 异常
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
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                // 授权服务器关闭basic认证
                .permitAll()
                .and()
                .logout()
                .logoutUrl(SecurityConstants.LOGOUT_URL)
                .logoutSuccessHandler(new OauthLogoutSuccessHandler())
                .addLogoutHandler(oauthLogoutHandler)
                .clearAuthentication(true)
                .and()
                .apply(openIdAuthenticationSecurityConfig)
                .and()
                .apply(mobileAuthenticationSecurityConfig)
                .and()
                .addFilterBefore(new LoginProcessSetTenantFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                // 解决不允许显示在iframe的问题
                .headers().frameOptions().disable().cacheControl();
        if(tenantProperties.getEnable()){
            // 解决不同租户单点登录时角色没有变化
            http.formLogin()
                    .loginPage(SecurityConstants.LOGIN_PAGE)
                    .and()
                    .addFilterAt(tenantUsernamePasswordAuthenticationFilter(authenticationManager),UsernamePasswordAuthenticationFilter.class)
                    .apply(tenantAuthenticationSecurityConfig);
        }else {
            http.formLogin()
                    .loginPage(SecurityConstants.LOGIN_PAGE)
                    .loginProcessingUrl(SecurityConstants.OAUTH_LOGIN_PRO_URL)
                    .successHandler(authenticationSuccessHandler);
        }
        // 基于密码等模式可以无session，不支持授权码模式
        if (authenticationEntryPoint != null){
            http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }else {
            // 授权码模式单独处理，需要session的支持，此模式可以支持所有oauth2的认证
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        }
    }

    /**
     * 全局用户信息
     * @param auth 用户授权信息
     * @throws Exception
     */
    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

}
