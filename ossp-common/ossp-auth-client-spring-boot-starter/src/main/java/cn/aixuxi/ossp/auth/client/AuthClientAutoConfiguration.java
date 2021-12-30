package cn.aixuxi.ossp.auth.client;

import cn.aixuxi.ossp.auth.client.properties.SecurityProperties;
import cn.aixuxi.ossp.auth.client.properties.TokenStoreProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * 鉴权自动配置
 * @author ruozhuliufeng
 */
@EnableConfigurationProperties({SecurityProperties.class, TokenStoreProperties.class})
@ComponentScan
public class AuthClientAutoConfiguration {
}
