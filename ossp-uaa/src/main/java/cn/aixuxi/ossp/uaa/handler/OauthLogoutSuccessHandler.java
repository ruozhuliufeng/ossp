package cn.aixuxi.ossp.uaa.handler;

import cn.aixuxi.ossp.common.model.Result;
import cn.aixuxi.ossp.common.utils.JsonUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 成功退出后处理
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 11:06
 **/
@Slf4j
public class OauthLogoutSuccessHandler implements LogoutSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        String redirectUri = request.getParameter("redirect_uri");
        if (StrUtil.isNotEmpty(redirectUri)){
            // 重定向到指定地址
            redirectStrategy.sendRedirect(request,response,redirectUri);
        }else {
            response.setStatus(HttpStatus.OK.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = response.getWriter();
            String jsonStr = JsonUtil.toJSONString(Result.succeed("登出成功"));
            writer.write(jsonStr);
            writer.flush();
        }
    }
}
