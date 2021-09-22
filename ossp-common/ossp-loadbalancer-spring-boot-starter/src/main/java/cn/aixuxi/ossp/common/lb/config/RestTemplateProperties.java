package cn.aixuxi.ossp.common.lb.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RestTemplate配置
 * @author ruozhuliufeng
 * @date 2021-09-18
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ossp.rest-template")
public class RestTemplateProperties {
    /**
     * 最大连接数
     */
    private int maxTotal = 200;
    /**
     * 同路由最大并发数
     */
    private int maxPerRoute = 50;
    /**
     * 读取超过时间 ms
     */
    private int readTimeout = 3500;
    /**
     * 链接超时时间 ms
     */
    private int cnnectTimeout = 1000;

}
