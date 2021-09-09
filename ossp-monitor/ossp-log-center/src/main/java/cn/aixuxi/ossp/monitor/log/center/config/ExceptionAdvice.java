package cn.aixuxi.ossp.monitor.log.center.config;

import cn.aixuxi.ossp.common.exception.DefaultExceptionAdvice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 统一异常处理
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-09 14:18
 **/
@ControllerAdvice
public class ExceptionAdvice extends DefaultExceptionAdvice {
}
