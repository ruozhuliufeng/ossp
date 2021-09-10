package cn.aixuxi.ossp.gateway.auth;

import cn.aixuxi.ossp.auth.client.service.impl.DefaultPermissionServiceImpl;
import cn.aixuxi.ossp.common.model.SysMenu;
import cn.aixuxi.ossp.gateway.feign.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * URL 权限认证
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-10 11:18
 **/
@Slf4j
@Component
public class PermissionAuthManager extends DefaultPermissionServiceImpl implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Resource
    private MenuService menuService;

    /**
     * 查询当前用户拥有的资源权限
     *
     * @param roleCodes 角色code列表，多个以','隔开
     * @return 资源权限列表
     */
    @Override
    public List<SysMenu> findMenuByRoleCodes(String roleCodes) {
        return menuService.findByRoleCodes(roleCodes);
    }


    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        return mono.map(auth->{
            ServerWebExchange exchange = authorizationContext.getExchange();
            ServerHttpRequest request = exchange.getRequest();
            boolean isPermission = super.hasPermission(auth,request.getMethodValue(),request.getURI().getPath());
            return new AuthorizationDecision(isPermission);
        }).defaultIfEmpty(new AuthorizationDecision(false));

    }
}
