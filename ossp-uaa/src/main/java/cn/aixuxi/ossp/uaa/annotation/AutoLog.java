package cn.aixuxi.ossp.uaa.annotation;

import cn.aixuxi.ossp.common.constant.SysConst;

import java.lang.annotation.*;

/**
 * 系统日志注解
 * @author ruozhuliufeng
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLog {

    /**
     * 日志内容
     * @return 日志内容
     */
    String value() default "";

    /**
     * 日志类型
     * @return 日志类型 1-登录日志 2-操作日志
     */
    int logType() default SysConst.LOG_TYPE_2;

    /**
     * 操作日志类型
     * @return 1：查询 2：添加 3：修改 4：删除
     */
    int operateType() default 0;
}
