package cn.aixuxi.ossp.gateway.auth;

import cn.aixuxi.ossp.auth.client.util.AuthUtils;
import cn.aixuxi.ossp.common.constant.SecurityConstants;
import cn.aixuxi.ossp.common.model.SysUser;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 认证成功处理
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-10 11:11
 **/
public class Oauth2AuthSuccessHandler implements ServerAuthenticationSuccessHandler {
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        MultiValueMap<String,String> headerValues = new LinkedMultiValueMap<>(4);
        Object principal = authentication.getPrincipal();
        // 客户端模式只返回一个clientId
        if (principal instanceof SysUser){
            SysUser user = (SysUser) authentication.getPrincipal();
            headerValues.add(SecurityConstants.USER_ID_HEADER,String.valueOf(user.getId()));
            headerValues.add(SecurityConstants.USER_HEADER,user.getUsername());
        }
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        String clientId = oAuth2Authentication.getOAuth2Request().getClientId();
        headerValues.add(SecurityConstants.TENANT_HEADER,clientId);
        headerValues.add(SecurityConstants.ROLE_HEADER, CollectionUtil.join(authentication.getAuthorities(),","));
        String accountType = AuthUtils.getAccountType(oAuth2Authentication.getUserAuthentication());
        headerValues.add(SecurityConstants.ACCOUNT_TYPE_HEADER,accountType);
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                .headers( h -> {
                    h.addAll(headerValues);
                })
                .build();
        ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
        return webFilterExchange.getChain().filter(build);
    }
}
