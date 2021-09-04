package cn.aixuxi.ossp.search.server.admin.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * ES 索引配置
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 13:58
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "ossp.indices")
@RefreshScope
public class IndexProperties {
    /**
     * 配置过滤的索引名：默认只显示这些索引
     */
    private String show;
}
