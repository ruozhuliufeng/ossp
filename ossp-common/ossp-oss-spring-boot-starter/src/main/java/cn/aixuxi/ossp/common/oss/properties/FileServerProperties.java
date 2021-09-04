package cn.aixuxi.ossp.common.oss.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 16:24
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = FileServerProperties.PREFIX)
public class FileServerProperties {
    public static final String PREFIX = "ossp.file-server";
    public static final String TYPE_FDFS = "fastdfs";
    public static final String TYPE_S3 = "s3";

    /**
     * 为以下两个值,指定不同的自动化配置<br>
     * s3: aws S3协议的存储(七牛云oss,阿里云oss，minio等)<br>
     * fastdfs：本地部署的fastDFS
     */
    private String type;

    S3Propertis s3 = new S3Propertis();

    /**
     * fastDfs配置
     */
    FdfsProperties fdfs = new FdfsProperties();
}
