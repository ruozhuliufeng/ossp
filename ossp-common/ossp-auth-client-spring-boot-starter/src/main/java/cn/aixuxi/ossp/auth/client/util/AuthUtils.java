package cn.aixuxi.ossp.auth.client.util;

import cn.aixuxi.ossp.common.constant.CommonConstant;
import cn.aixuxi.ossp.common.model.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Enumeration;

/**
 * 认证授权相关工具类
 * @author ruozhuliufeng
 * @date 2021-08-05
 */
@Slf4j
public class AuthUtils {
    private AuthUtils(){
        throw new IllegalStateException("Untity Class");
    }

    private static final String BASIC_ = "Basic ";

    /**
     * 获取request(head/param)中的token
     * @param request 请求
     * @return
     */
    public static String extractToken(HttpServletRequest request){
        String token = extractHeaderToken(request);
        if (token == null){
            token = request.getParameter(OAuth2AccessToken.ACCESS_TOKEN);
            if (token == null){
                log.debug("参数中未携带Token!请检查是否符合OAuth2请求格式");
            }
        }
        return token;
    }

    /**
     * 解析header中的token
     * @param request 请求
     * @return token
     */
    private static String extractHeaderToken(HttpServletRequest request){
        Enumeration<String> headers = request.getHeaders(CommonConstant.TOKEN_HEADER);
        while (headers.hasMoreElements()){
            String value = headers.nextElement();
            if ((value.startsWith(OAuth2AccessToken.BEARER_TYPE))){
                String authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0){
                    authHeaderValue = authHeaderValue.substring(0,commaIndex);
                }
                return authHeaderValue;
            }
        }
        return null;
    }

    /**
     * 从header 请求中获取clientId:clientSecret
     * @param request 请求
     * @return
     */
    public static String[] extractClient(HttpServletRequest request){
        String header = request.getHeader(CommonConstant.TOKEN_HEADER);
        if (header==null || !header.startsWith(BASIC_)){
            throw new UnapprovedClientAuthenticationException("请求头中client信息为空！");
        }
        return extractHeaderClient(header);
    }

    /**
     * header请求中的clientId:clientSecret
     * @param header 请求参数
     * @return
     */
    private static String[] extractHeaderClient(String header) {
        byte[] base64Client = header.substring(BASIC_.length()).getBytes(StandardCharsets.UTF_8);
        byte[] decoded = Base64.getDecoder().decode(base64Client);
        String clientStr = new String(decoded,StandardCharsets.UTF_8);
        String[] clientArr = clientStr.split(":");
        if (clientArr.length!=2){
            throw new RuntimeException("授权信息无效！");
        }
        return clientArr;
    }

    private static String getUsername(Authentication authentication){
        Object principal = authentication.getPrincipal();
        String username = null;
        if (principal instanceof SysUser){
            username = ((SysUser)principal).getUsername();
        }else if (principal instanceof String){
            username = (String) principal;
        }
        return username;
    }
}
