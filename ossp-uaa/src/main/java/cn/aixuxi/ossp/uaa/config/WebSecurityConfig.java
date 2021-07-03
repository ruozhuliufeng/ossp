package cn.aixuxi.ossp.uaa.config;


import cn.aixuxi.auth.server.filter.CaptchaFilter;
import cn.aixuxi.auth.server.filter.JwtAuthenticationFilter;
import cn.aixuxi.auth.server.filter.JwtLogoutSuccessHandler;
import cn.aixuxi.auth.server.handler.JwtAccessDeniedHandler;
import cn.aixuxi.auth.server.handler.JwtAuthenticationEntryPoint;
import cn.aixuxi.auth.server.handler.LoginFaliureHandler;
import cn.aixuxi.auth.server.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 类描述
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021/5/17 22:20
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    // 登录认证失败处理
    @Autowired
    private LoginFaliureHandler loginFaliureHandler;
    // 登录成功处理
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    // 验证码过滤
    @Autowired
    private CaptchaFilter captchaFilter;

    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception{
        return new JwtAuthenticationFilter(authenticationManager());
    }

    // 放行资源(不需要认证即可访问)
    private static final String[] URL_WHITELIST = {
            "/login",
            "/logout",
            "/captcha",
            "/favicon.ico",
            "/static/**"
    };

    /**
     * 功能描述: 认证管理器
     * @author : ruozhuliufeng
     * @date : 2021/5/17 22:51
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 功能描述: 密码编码器
     * @author : ruozhuliufeng
     * @date : 2021/5/17 22:52
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    /**
     * 功能描述: 安全拦截机制
     * @author : ruozhuliufeng
     * @date : 2021/5/17 22:53
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()// 关闭CSRF
                // 登录配置
                .formLogin()
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFaliureHandler)

                .and()
                .logout()
                .logoutSuccessHandler(jwtLogoutSuccessHandler)

                // 禁用session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 配置拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()
                .anyRequest().authenticated()

                // 异常处理
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 配置自定义的过滤器
                .and()
                .addFilter(jwtAuthenticationFilter())
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
