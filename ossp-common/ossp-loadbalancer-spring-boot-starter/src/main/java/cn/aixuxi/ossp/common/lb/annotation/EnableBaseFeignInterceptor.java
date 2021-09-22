package cn.aixuxi.ossp.common.lb.annotation;

import cn.aixuxi.ossp.common.lb.config.FeignInterceptorConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启feign拦截器，传递数据给下游服务，只包含基础数据
 * @author ruozhuliufeng
 * @date 2021-09-18
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(FeignInterceptorConfig.class)
public @interface EnableBaseFeignInterceptor {
}
