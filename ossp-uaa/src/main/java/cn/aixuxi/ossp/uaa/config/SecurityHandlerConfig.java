package cn.aixuxi.ossp.uaa.config;

import cn.aixuxi.ossp.uaa.exception.ValidateCodeException;
import cn.aixuxi.ossp.uaa.handler.OauthLogoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.UnsupportedResponseTypeException;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证错误处理
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 15:57
 **/
@Slf4j
@Configuration
public class SecurityHandlerConfig {

    @Bean
    public OauthLogoutHandler oauthLogoutHandler(){
        return new OauthLogoutHandler();
    }

    @Bean
    public WebResponseExceptionTranslator webResponseExceptionTranslator(){
        return new DefaultWebResponseExceptionTranslator(){
            private static final String BAD_MSG = "坏的拼争";
            private static final String BAD_MSG_EN = "Bad Credentials";

            @Override
            public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
                OAuth2Exception oAuth2Exception;
                if (e.getMessage() !=null
                        && (BAD_MSG.equals(e.getMessage())
                            || BAD_MSG_EN.equals(e.getMessage()))){
                    oAuth2Exception = new InvalidGrantException("用户名或密码错误",e);
                }else if (e instanceof InternalAuthenticationServiceException
                    || e instanceof ValidateCodeException){
                    oAuth2Exception = new InvalidGrantException(e.getMessage(),e);
                }else if (e instanceof OAuth2Exception){
                    oAuth2Exception = (OAuth2Exception) e;
                }else {
                    oAuth2Exception = new UnsupportedResponseTypeException("服务内部错误",e);
                }
                ResponseEntity<OAuth2Exception> response = super.translate(oAuth2Exception);
                ResponseEntity.status(oAuth2Exception.getHttpErrorCode());
                response.getBody().addAdditionalInformation("resp_code",oAuth2Exception.getHttpErrorCode()+"");
                response.getBody().addAdditionalInformation("resp_msg",oAuth2Exception.getMessage());
                return response;
            }
        };
    }

    /**
     * 登录成功处理
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler(){
        return new SavedRequestAwareAuthenticationSuccessHandler(){
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
                super.onAuthenticationSuccess(request, response, authentication);
            }
        };
    }
}
