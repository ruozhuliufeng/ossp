package cn.aixuxi.ossp.common.oss.properties;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Getter;
import lombok.Setter;

/**
 * AWS S3协议配置
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 16:28
 **/
@Getter
@Setter
public class S3Propertis {

    /**
     * 用户名
     */
    private String accessKey;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 访问端点
     */
    private String endpoint;

    /**
     * bucket名称
     */
    private String bucketName;

    /**
     * 区域
     */
    private String region;

    /**
     * path-style
     */
    private Boolean pathStyleAccessEnabled = true;
}
