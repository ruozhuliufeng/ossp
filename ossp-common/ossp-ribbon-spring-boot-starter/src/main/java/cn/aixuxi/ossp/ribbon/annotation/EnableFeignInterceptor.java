package cn.aixuxi.ossp.ribbon.annotation;

import cn.aixuxi.ossp.ribbon.config.FeignHttpInterceptorConfig;
import cn.aixuxi.ossp.ribbon.config.FeignInterceptorConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启feign拦截器，传递数据给下游服务，包含基础数据和http的相关数据
 * @author ruozhuliufeng
 * @date 2021-09-02
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({FeignHttpInterceptorConfig.class, FeignInterceptorConfig.class})
public @interface EnableFeignInterceptor {
}
