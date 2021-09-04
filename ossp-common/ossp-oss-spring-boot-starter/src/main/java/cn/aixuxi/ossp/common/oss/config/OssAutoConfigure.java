package cn.aixuxi.ossp.common.oss.config;

import cn.aixuxi.ossp.common.oss.properties.FileServerProperties;
import cn.aixuxi.ossp.common.oss.template.FdfsTemplate;
import cn.aixuxi.ossp.common.oss.template.S3Template;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * 自动配置注入
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 16:30
 **/
@EnableConfigurationProperties(FileServerProperties.class)
@Import({FdfsTemplate.class, S3Template.class})
public class OssAutoConfigure {
}
