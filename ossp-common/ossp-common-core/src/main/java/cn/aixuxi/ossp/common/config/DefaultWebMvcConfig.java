package cn.aixuxi.ossp.common.config;

import cn.aixuxi.ossp.common.feign.UserService;
import cn.aixuxi.ossp.common.resolver.ClientArgumentResolver;
import cn.aixuxi.ossp.common.resolver.TokenArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 默认SpringMVC拦截器
 * @author ruozhuliufeng
 * @date 2021-09-01
 */
public class DefaultWebMvcConfig implements WebMvcConfigurer {

    @Lazy
    @Autowired
    private UserService userService;

    /**
     * Token参数解析
     * @param resolvers 解析类
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // 注入用户信息
        resolvers.add(new TokenArgumentResolver(userService));
        // 注入应用信息
        resolvers.add(new ClientArgumentResolver());
    }
}
