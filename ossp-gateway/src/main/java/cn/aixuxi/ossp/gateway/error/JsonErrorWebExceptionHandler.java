package cn.aixuxi.ossp.gateway.error;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.web.reactive.function.server.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义异常处理
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-10 16:08
 **/
public class JsonErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {
    public JsonErrorWebExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    /**
     * 获取异常属性
     *
     * @param request 请求
     * @param options 参数
     * @return Map
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = super.getError(request);
        return responseError(request, error);
    }

    /**
     * 指定响应处理方法为JSON处理的方法
     *
     * @param errorAttributes 参数
     * @return 返回
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        Integer httpStatus = (Integer) errorAttributes.remove("httpStatus");
        return httpStatus != null ? httpStatus : HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    /**
     * 构建返回的JSON数据格式
     *
     * @param request 请求
     * @param error   错误信息
     * @return JSON
     */
    private Map<String, Object> responseError(ServerRequest request, Throwable error) {
        String errorMessage = buildMessage(request, error);
        int httpStatus = getHttpStatus(error);
        Map<String, Object> map = new HashMap<>();
        map.put("resp_code", 1);
        map.put("resp_msg", errorMessage);
        map.put("datas", null);
        map.put("httpStatus", httpStatus);
        return map;
    }

    /**
     * 获取Http状态
     *
     * @param error 错误参数
     * @return HTTP装填
     */
    private int getHttpStatus(Throwable error) {
        int httpStatus;
        if (error instanceof InvalidTokenException) {
            InvalidTokenException tokenException = (InvalidTokenException) error;
            httpStatus = tokenException.getHttpErrorCode();
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        return httpStatus;
    }

    /**
     * 构建异常信息
     *
     * @param request 请求
     * @param error   错误信息
     * @return 异常信息
     */
    private String buildMessage(ServerRequest request, Throwable error) {
        StringBuilder message = new StringBuilder("处理请求失败：[");
        message.append(request.methodName());
        message.append(" ");
        message.append(request.uri());
        message.append("]");
        if (error != null) {
            message.append(": ");
            message.append(error.getMessage());
        }
        return message.toString();
    }
}
