package cn.aixuxi.ossp.gateway.auth;

import cn.aixuxi.ossp.common.utils.WebfluxResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 403拒绝访问异常处理，转换为JSON
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-10 11:08
 **/
@Slf4j
public class JsonAccessDeniedHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, AccessDeniedException e) {
        return WebfluxResponseUtil.responseFailed(serverWebExchange, HttpStatus.FORBIDDEN.value(),e.getMessage());
    }
}
