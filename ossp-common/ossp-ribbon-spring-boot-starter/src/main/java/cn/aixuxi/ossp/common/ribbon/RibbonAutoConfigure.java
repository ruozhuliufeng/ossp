package cn.aixuxi.ossp.common.ribbon;

import cn.aixuxi.ossp.common.ribbon.config.RestTemplateProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.DefaultPropertiesFactory;
import org.springframework.context.annotation.Bean;

/**
 * Ribbon 扩展配置类
 * @author ruozhuliufeng
 * @date 2021-09-02
 */
@EnableConfigurationProperties(RestTemplateProperties.class)
public class RibbonAutoConfigure {

    @Bean
    public DefaultPropertiesFactory defaultPropertiesFactory(){
        return new DefaultPropertiesFactory();
    }
}
