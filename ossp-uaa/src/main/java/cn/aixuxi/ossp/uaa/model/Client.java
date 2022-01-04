package cn.aixuxi.ossp.uaa.model;

import cn.aixuxi.ossp.common.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客户端详情
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 11:15
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("oauth_client_details")
public class Client extends BaseEntity {
    private static final long serialVersionUID = -8185413579135897885L;

    /**
     * 应用id
     */
    private String clientId;

    /**
     * 应用名称
     */
    private String clientName;

    /**
     * 资源id集合
     */
    private String resourceIds = "";
    /**
     * 客户端密钥
     */
    private String clientSecret;
    /**
     * 客户端密钥(String)
     */
    private String clientSecretStr;
    /**
     * 授权方位
     */
    private String scope = "all";
    /**
     * 授权类型
     */
    private String authorizedGrantTypes = "authorization_code,password,refresh_token,client_credentials";
    /**
     * 回调地址
     */
    private String webServerRedirectUri;
    /**
     * 授权信息
     */
    private String authorities = "";
    /**
     * accessToken有效时长
     */
    @TableField(value = "access_token_validity")
    private Integer accessTokenValiditySeconds = 18000;
    /**
     * refreshToken有效时长
     */
    @TableField(value = "refresh_token_validity")
    private Integer refreshTokenValiditySeconds = 28800;
    /**
     * 附加信息
     */
    private String additionalInformation = "{}";
    /**
     * 是否自动授权
     */
    private String autoapprove = "true";
    /**
     * 是否支持id_token
     */
    private Boolean supportIdToken = true;
    /**
     * Token有效时长(s)
     */
    @TableField(value = "id_token_validity")
    private Integer idTokenValiditySeconds = 60;
}
