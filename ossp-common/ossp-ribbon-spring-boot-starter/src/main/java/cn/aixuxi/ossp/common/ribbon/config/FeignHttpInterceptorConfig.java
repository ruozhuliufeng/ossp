package cn.aixuxi.ossp.common.ribbon.config;

import cn.aixuxi.ossp.common.constant.CommonConstant;
import cn.aixuxi.ossp.common.constant.SecurityConstants;
import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Feign 拦截器，只包含http相关数据
 * @author ruozhuliufeng
 * @date 2021-09-02
 */
public class FeignHttpInterceptorConfig {
    protected List<String> reqeuestHeaders = new ArrayList<>();

    @PostConstruct
    public void initialize(){
        reqeuestHeaders.add(SecurityConstants.USER_ID_HEADER);
        reqeuestHeaders.add(SecurityConstants.USER_ID_HEADER);
        reqeuestHeaders.add(SecurityConstants.ROLE_HEADER);
        reqeuestHeaders.add(CommonConstant.O_S_S_P_VERSION);
    }

    public RequestInterceptor httpFeignInterceptor(){
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null){
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames !=null){
                    String headerName;
                    String headerValue;
                    while (headerNames.hasMoreElements()){
                        headerName = headerNames.nextElement();
                        if (reqeuestHeaders.contains(headerName)){
                            headerValue = request.getHeader(headerName);
                            requestTemplate.header(headerName,headerValue);
                        }
                    }
                }
                // 传递access_token，无网络隔离时需要传递
               /* String token = extractHeaderToken(request);
                if (StrUtil.isEmpty(token)){
                    token = request.getParameter(CommonConstant.ACCESS_TOKEN);
                }
                if (StrUtil.isNotEmpty(token)){
                    requestTemplate.header(CommonConstant.TOKEN_HEADER,CommonConstant.BEARER_TYPE +" "+token);
                }*/
            }
        };
    }

    /**
     * 解析header中的token
     * @param request 请求
     * @return token
     */
    private String extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(CommonConstant.TOKEN_HEADER);
        while (headers.hasMoreElements()){
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(CommonConstant.BEARER_TYPE.toLowerCase()))){
                String authHeaderValue = value.substring(CommonConstant.BEARER_TYPE.length()).trim();
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex>0){
                    authHeaderValue = authHeaderValue.substring(0,commaIndex);
                }
                return authHeaderValue;
            }
        }
        return null;
    }
}
