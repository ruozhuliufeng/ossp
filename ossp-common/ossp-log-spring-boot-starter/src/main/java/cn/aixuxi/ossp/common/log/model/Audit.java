package cn.aixuxi.ossp.common.log.model;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 审计日志
 * @author ruozhuliufeng
 * @date 2021-07-17
 */
@Getter
@Setter
public class Audit {

    /**
     * 操作时间
     */
    private LocalDateTime timestamp;
    /**
     * 应用名
     */
    private String applicationName;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 租户id
     */
    private String clientId;

    /**
     * 操作信息
     */
    private String operation;
}
