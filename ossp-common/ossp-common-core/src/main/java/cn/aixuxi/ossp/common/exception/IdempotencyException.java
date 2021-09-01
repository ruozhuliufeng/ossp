package cn.aixuxi.ossp.common.exception;

/**
 * 幂等性异常
 * @author ruozhuliufeng
 * @date 2021-09-01
 */
public class IdempotencyException extends RuntimeException{
    private static final long serialVersionUID = 6610083281801529147L;

    public IdempotencyException(String message){
        super(message);
    }
}

