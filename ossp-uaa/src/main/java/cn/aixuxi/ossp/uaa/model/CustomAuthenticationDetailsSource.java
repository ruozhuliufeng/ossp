package cn.aixuxi.ossp.uaa.model;

import cn.aixuxi.ossp.auth.client.token.CustomWebAuthenticationDetails;
import cn.aixuxi.ossp.common.constant.SecurityConstants;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 表单登录的认证信息对象
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-23 9:54
 **/
@Component
public class CustomAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, CustomWebAuthenticationDetails> {
    @Override
    public CustomWebAuthenticationDetails buildDetails(HttpServletRequest request) {
        String remoteAddress = request.getRemoteAddr();
        HttpSession session = request.getSession(false);
        String sessionId = session != null ? session.getId() : null;
        String accountType = request.getParameter(SecurityConstants.ACCOUNT_TYPE_PARAM_NAME);
        return new CustomWebAuthenticationDetails(remoteAddress,sessionId,accountType);
    }
}
