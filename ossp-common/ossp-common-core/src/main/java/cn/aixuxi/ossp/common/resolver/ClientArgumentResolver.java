package cn.aixuxi.ossp.common.resolver;

import cn.aixuxi.ossp.common.annotatioin.LoginClient;
import cn.aixuxi.ossp.common.constant.SecurityConstants;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * head中的应用参数注入到clientId中
 * @author ruozhuliufeng
 * @date 2021-09-01
 */
@Slf4j
public class ClientArgumentResolver implements HandlerMethodArgumentResolver {
    /**
     * 入参筛选
     * @param methodParameter 参数集合
     * @return 格式化后的参数
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(LoginClient.class) && methodParameter.getParameterType().equals(String.class);
    }

    /**
     * @param methodParameter 入参集合
     * @param modelAndViewContainer model view
     * @param nativeWebRequest web相关
     * @param webDataBinderFactory 入参解析
     * @return 包装对象
     * @throws Exception 异常
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String clientId = request.getHeader(SecurityConstants.TENANT_HEADER);
        if (StrUtil.isBlank(clientId)){
            log.warn("参数有误！客户端id为空！");
        }
        return clientId;
    }
}
