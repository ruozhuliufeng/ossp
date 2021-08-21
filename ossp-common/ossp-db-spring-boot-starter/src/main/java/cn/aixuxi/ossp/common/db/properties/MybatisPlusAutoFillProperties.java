package cn.aixuxi.ossp.common.db.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Mybatis-Plus 自动注入配置
 * @author ruozhuliufeng
 * @date 2021-08-21
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ossp.mybatis-plus.auto-fill")
@RefreshScope
public class MybatisPlusAutoFillProperties {

    /**
     * 是否开启自动填充字段
     */
    private Boolean enabled = true;
    /**
     * 是否开启插入填充
     */
    private Boolean enableInsertFill = true;
    /**
     * 是否开启更新填充
     */
    private Boolean enableUpdateFill = true;
    /**
     * 创建时间字段名
     */
    private String createTimeField = "createTime";
    /**
     * 更新时间字段名
     */
    private String updateTimeField = "updateTime";
}
