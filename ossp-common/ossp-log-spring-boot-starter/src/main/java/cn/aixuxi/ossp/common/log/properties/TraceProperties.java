package cn.aixuxi.ossp.common.log.properties;

import cn.hutool.extra.spring.SpringUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 日志链路追踪配置
 * @author ruozhuliufeng
 * @date 2021-07-17
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ossp.trace")
@RefreshScope
public class TraceProperties {

    /**
     * 是否开启日志链路追踪
     */
    private Boolean enable = false;
}
