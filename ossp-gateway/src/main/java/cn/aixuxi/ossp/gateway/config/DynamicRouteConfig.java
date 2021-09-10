package cn.aixuxi.ossp.gateway.config;

import cn.aixuxi.ossp.gateway.route.NacosRouteDefinitionRepository;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 动态路由配置
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-10 13:33
 **/
@Configuration
@ConditionalOnProperty(prefix = "ossp.gateway.dynamicRoute",name = "enabled",havingValue = "true")
public class DynamicRouteConfig {
    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * Nacos实现方式
     */
    @Configuration
    @ConditionalOnProperty(prefix = "ossp.gateway.dynamicRoute",name = "dataType",havingValue = "nacos",matchIfMissing = true)
    public class NacosDynRoute{
        @Autowired
        private NacosConfigProperties nacosConfigProperties;

        @Bean
        public NacosRouteDefinitionRepository nacosRouteDefinitionRepository(){
            return new NacosRouteDefinitionRepository(publisher,nacosConfigProperties);
        }
    }
}
