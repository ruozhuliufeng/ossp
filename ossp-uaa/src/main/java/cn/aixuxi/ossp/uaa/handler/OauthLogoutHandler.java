package cn.aixuxi.ossp.uaa.handler;

import cn.aixuxi.ossp.auth.client.util.AuthUtils;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Oauth 退出处理
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 10:54
 **/
@Slf4j
public class OauthLogoutHandler implements LogoutHandler {
    @Autowired
    private TokenStore tokenStore;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Assert.notNull(tokenStore,"tokenStore必须注入");
        String token = request.getParameter("token");
        if (StrUtil.isEmpty(token)){
            token = AuthUtils.extractToken(request);
        }
        if (StrUtil.isNotEmpty(token)){
            OAuth2AccessToken existingAccessToen = tokenStore.readAccessToken(token);
            OAuth2RefreshToken refreshToken;
            if (existingAccessToen != null){
                if (existingAccessToen.getRefreshToken() != null){
                    log.info("删除刷新token:{}!",existingAccessToen.getRefreshToken());
                    refreshToken = existingAccessToen.getRefreshToken();
                    tokenStore.removeRefreshToken(refreshToken);
                }
                log.info("删除已存在的认证Token：{}",existingAccessToen);
                tokenStore.removeAccessToken(existingAccessToen);
            }
        }

    }
}
