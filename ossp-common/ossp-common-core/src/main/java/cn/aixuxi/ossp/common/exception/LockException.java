package cn.aixuxi.ossp.common.exception;

/**
 * 分布式锁异常
 * @author ruozhuliufeng
 * @date 2021-09-01
 */
public class LockException extends RuntimeException{

    private static final long serialVersionUID = 6610083281801529147L;

    public LockException(String message){
        super(message);
    }
}
