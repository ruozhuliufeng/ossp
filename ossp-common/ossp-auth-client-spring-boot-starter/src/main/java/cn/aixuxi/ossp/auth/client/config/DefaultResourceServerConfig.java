package cn.aixuxi.ossp.auth.client.config;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 默认资源服务配置
 * @author ruozhuliufeng
 * @date 2021-08-05
 */
@Import(DefaultSecurityHandlerConfig.class)
public class DefaultResourceServerConfig extends ResourceServerConfigurerAdapter {
}
