package cn.aixuxi.ossp.uaa.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Token传输VO
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 11:13
 **/
@Getter
@Setter
public class TokenVO implements Serializable {
    private static final long serialVersionUID = -6656955957477645319L;

    /**
     * token的值
     */
    private String tokenValue;
    /**
     * 到期时间
     */
    private Date expiration;
    /**
     * 用户名
     */
    private String username;
    /**
     * 所属应用
     */
    private String clientId;
    /**
     * 授权类型
     */
    private String grantType;
}
