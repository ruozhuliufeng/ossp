package cn.aixuxi.ossp.auth.client.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 验证码配置
 */
@Getter
@Setter
public class ValidateCodeProperties {

    /**
     * 设置认证通过时不需要验证码的clientId
     */
    private String[] ignoreClientCode = {};
}
