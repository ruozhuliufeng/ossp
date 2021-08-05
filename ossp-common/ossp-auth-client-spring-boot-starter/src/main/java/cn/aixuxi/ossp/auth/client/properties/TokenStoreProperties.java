package cn.aixuxi.ossp.auth.client.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Token缓存配置
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(prefix = "ossp.oauth2.token.store")
public class TokenStoreProperties {

    /**
     * Token存储类型(redis/db/authJwt/resJwt)
     */
    private String type = "redis";
}
