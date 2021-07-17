package cn.aixuxi.ossp.log.config;

import cn.aixuxi.ossp.log.properties.AuditLogProperties;
import cn.aixuxi.ossp.log.properties.LogDbProperties;
import cn.aixuxi.ossp.log.properties.TraceProperties;
import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 日志自动配置
 * @author ruozhuliufeng
 * @date 2021-07-17
 */
@EnableConfigurationProperties({AuditLogProperties.class, TraceProperties.class})
public class LogAutoConfigure {


    @Configuration
    @ConditionalOnClass(HikariConfig.class)
    @EnableConfigurationProperties(LogDbProperties.class)
    public static class LogDbAutoConfigure{}
}
