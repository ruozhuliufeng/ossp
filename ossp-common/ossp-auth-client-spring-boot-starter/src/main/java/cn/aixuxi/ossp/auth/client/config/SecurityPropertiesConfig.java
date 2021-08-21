package cn.aixuxi.ossp.auth.client.config;

import cn.aixuxi.ossp.auth.client.properties.SecurityPropertis;
import cn.aixuxi.ossp.auth.client.properties.TokenStoreProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 资源配置
 * @author ruozhuliufeng
 */
@EnableConfigurationProperties({SecurityPropertis.class, TokenStoreProperties.class})
public class SecurityPropertiesConfig {
}
