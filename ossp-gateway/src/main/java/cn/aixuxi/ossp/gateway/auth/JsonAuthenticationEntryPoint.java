package cn.aixuxi.ossp.gateway.auth;

import cn.aixuxi.ossp.common.utils.WebfluxResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 401 未授权异常处理，转换为JSON
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-10 11:09
 **/
@Slf4j
public class JsonAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange serverWebExchange, AuthenticationException e) {
        return WebfluxResponseUtil.responseFailed(serverWebExchange, HttpStatus.UNAUTHORIZED.value(),e.getMessage());
    }
}
