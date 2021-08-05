package cn.aixuxi.ossp.auth.client.properties;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.Getter;
import lombok.Setter;

/**
 * 认证配置
 * @author ruozhuliufeng
 * @date 2021-08-05
 */
@Getter
@Setter
public class AuthProperties {

    /**
     * 配置需要认证的url(默认不需要配置)，优先级大于忽略认证配置
     * `ossp.security.ignore.httpUrls`，
     * 如果同一个url同时配置了忽略认证和需要认证，则该url还是会被认证
     */
    private String[] httpUrls = {};

    /**
     * token自动续签配置(暂时只有redis实现)
     */
    private RenewProperties renew = new RenewProperties();

    /**
     * url权限配置
     */
    private UrlPermissionProperties urlPermission = new UrlPermissionProperties();
}
