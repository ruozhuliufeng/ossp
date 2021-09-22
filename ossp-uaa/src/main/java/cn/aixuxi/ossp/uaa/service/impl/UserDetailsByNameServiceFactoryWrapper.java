package cn.aixuxi.ossp.uaa.service.impl;

import cn.aixuxi.ossp.uaa.service.OsspUserDetailsService;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.Assert;

/**
 * 重写UserDetaisByNameServiceWrapper，支持多账户类型
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-22 16:51
 **/
public class UserDetailsByNameServiceFactoryWrapper <T extends Authentication> implements
        AuthenticationUserDetailsService<T>, InitializingBean {

    @Setter
    private UserDetailServiceFactory userDetailServiceFactory;

    public UserDetailsByNameServiceFactoryWrapper() {
    }

    public UserDetailsByNameServiceFactoryWrapper(UserDetailServiceFactory userDetailServiceFactory) {
        Assert.notNull(userDetailServiceFactory,"用户服务工厂不能为空！");
        this.userDetailServiceFactory = userDetailServiceFactory;
    }

    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties
     * and satisfied, {@code ApplicationContextAware} etc.
     * <p>This method allows the bean instance to perform validation of its overall
     * configuration and final initialization when all bean properties have been set.
     *
     * @throws Exception in the event of misconfiguration (such as failure to set an
     *                   essential property) or if initialization fails for any other reason
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailServiceFactory,"用户服务工厂必须被注入");
    }

    @Override
    public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
        OsspUserDetailsService userDetailsService;
        if (authentication instanceof PreAuthenticatedAuthenticationToken){
            userDetailsService = this.userDetailServiceFactory.getService((Authentication) authentication.getPrincipal());
        }else {
            userDetailsService = this.userDetailServiceFactory.getService(authentication);
        }
        return userDetailsService.loadUserByUsername(authentication.getName());
    }
}
