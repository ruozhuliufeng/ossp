package cn.aixuxi.ossp.search.client.annotation;

import cn.aixuxi.ossp.search.client.client.feign.fallback.SearchServiceFallbackFactory;
import cn.aixuxi.ossp.search.client.client.service.impl.QueryServiceImpl;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制是否加载搜索中心客户端的Service
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 11:08
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableFeignClients(basePackages = "cn.aixuxi.ossp")
@Import({SearchServiceFallbackFactory.class, QueryServiceImpl.class})
public @interface EnableSearchClient {
}
