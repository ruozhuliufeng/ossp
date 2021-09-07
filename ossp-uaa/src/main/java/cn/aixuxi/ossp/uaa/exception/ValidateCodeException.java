package cn.aixuxi.ossp.uaa.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常处理
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 17:37
 **/
public class ValidateCodeException extends AuthenticationException {
    private static final long serialVersionUID = -7285211528095468156L;

    public ValidateCodeException(String msg){
        super(msg);
    }
}
