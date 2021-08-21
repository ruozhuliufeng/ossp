package cn.aixuxi.ossp.auth.client.config;

import cn.aixuxi.ossp.auth.client.properties.SecurityPropertis;
import cn.aixuxi.ossp.auth.client.properties.TokenStoreProperties;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.annotation.Resource;

/**
 * 默认资源服务配置
 *
 * @author ruozhuliufeng
 * @date 2021-08-05
 */
@Import(DefaultSecurityHandlerConfig.class)
public class DefaultResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Resource
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Resource
    private OAuth2WebSecurityExpressionHandler expressionHandler;

    @Resource
    private OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler;

    @Autowired
    private SecurityPropertis securityPropertis;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore)
                .stateless(true)
                .authenticationEntryPoint(authenticationEntryPoint)
                .expressionHandler(expressionHandler)
                .accessDeniedHandler(oAuth2AccessDeniedHandler);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl = setHttp(http)
                .authorizeRequests()
                // 放行无需认证的访问地址
                .antMatchers(securityPropertis.getIgnore().getUrls()).permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest();
        setAuthenticate(authorizedUrl);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .httpBasic().disable()
                .headers()
                .frameOptions().disable()
                .and()
                // 关闭csrf
                .csrf().disable();
    }


    /**
     * url权限控制，默认是认证就通过，可以重写实现个性化
     *
     * @param authorizedUrl 权限控制地址
     * @return HttpSecurity
     */
    public HttpSecurity setAuthenticate(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl) {
        return authorizedUrl.authenticated().and();
    }

    /**
     * 留给子类重写扩展功能
     *
     * @param httpSecurity 类
     * @return http
     */
    public HttpSecurity setHttp(HttpSecurity httpSecurity) {
        return httpSecurity;
    }
}
