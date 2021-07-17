package cn.aixuxi.ossp.log.annotation;

import java.lang.annotation.*;

/**
 * 日志注解
 * @author ruozhuliufeng
 * @date 2021-07-17
 */
@Documented
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {
    /**
     * 操作信息
     * @return 操作信息
     */
    String operation();
}
