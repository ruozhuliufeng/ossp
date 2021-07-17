package cn.aixuxi.ossp.common.annotatioin;

import java.lang.annotation.*;

/**
 * 请求否方法参数上添加该注解，则注入当前登录账号的应用id
 * 例子：public void test(@LoginClient String clientId) // 则注入webApp
 * @author ruozhuliufeng
 * @date 2021-07-17
 */
@Target(ElementType.PARAMETER)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginClient {
}
