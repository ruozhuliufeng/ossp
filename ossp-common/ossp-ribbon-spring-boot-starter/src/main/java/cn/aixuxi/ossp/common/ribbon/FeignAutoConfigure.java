package cn.aixuxi.ossp.common.ribbon;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * Feign统一配置
 * @author ruozhuliufeng
 * @date 2021-09-02
 */
public class FeignAutoConfigure {

    /**
     * Feign 日志级别
     * @return 日志级别
     */
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
