package cn.aixuxi.ossp.common.annotatioin;

import java.lang.annotation.*;

/**
 * 请求的方法参数SysUser上添加该注解，则注入当前登录人信息
 *  例1： public void test(@LoginUser SysUser user) // 只有username和role
 *  例2： public void test(@LoginUser(isFull = true) SysUser user) // 能获取SysUser对象的所有信息
 * @author ruozhuliufeng
 * @date 2021-07-17
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {

    /**
     * 是否查询SysUser 对象所有信息，true则通过rpc接口查询
     * @return false 不查询，true 通过rpc接口查询
     */
    boolean isFull() default false;
}
