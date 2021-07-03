package cn.aixuxi.ossp.uaa.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 类描述 oauth客户端
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021/5/29 13:10
 */
@TableName("oauth_client_details")
@Data
public class OauthClientDetails extends BaseEntity {
    /**
     * 客户端标识
     */
    private String clientId;
    /**
     * 资源标识
     */
    private String resourceIds;
    /**
     * 客户端标密钥
     */
    private String clientSecret;
    /**
     * 客户端授权范围
     */
    private String scope;
    /**
     * 客户端支持授权类型
     */
    private String authorizedGrantTypes;
    /**
     * 客户端重定向地址
     */
    private String webServerRedirectUri;
    /**
     * 客户端权限
     */
    private String authorities;
    /**
     * 客户端token过期时间(s)
     */
    private String accessTokenValidity;
    /**
     * 客户端刷新token过期时间(s)
     */
    private String refreshTokenValidity;
    /**
     * 客户端
     */
    private String additionalInformation;
    /**
     * 客户端
     */
    private String archived;
    /**
     * 是否信任客户端
     */
    private String trusted;
    /**
     * 客户端自动授权
     */
    private String autoapprove;
}
