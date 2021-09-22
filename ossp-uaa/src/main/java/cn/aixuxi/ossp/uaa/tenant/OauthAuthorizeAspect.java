package cn.aixuxi.ossp.uaa.tenant;

import cn.aixuxi.ossp.auth.client.token.TenantUsernamePasswordAuthenticationToken;
import cn.aixuxi.ossp.common.context.TenantContextHolder;
import cn.aixuxi.ossp.common.feign.UserService;
import cn.aixuxi.ossp.common.model.LoginAppUser;
import cn.aixuxi.ossp.uaa.service.impl.UserDetailServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;

/**
 * /oauth/authorize拦截器，解决不同租户单点登录时角色没变化
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 13:59
 **/
@Slf4j
@Component
@Aspect
public class OauthAuthorizeAspect {

    private final UserDetailServiceFactory userDetailServiceFactory;

    public OauthAuthorizeAspect(UserDetailServiceFactory userDetailServiceFactory) {
        this.userDetailServiceFactory = userDetailServiceFactory;
    }
    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.authorize(..))")
    public Object doArroundMethod(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] args = joinPoint.getArgs();
        Map<String,String> parameters = (Map<String, String>) args[1];
        Principal principal = (Principal) args[3];
        if (principal instanceof TenantUsernamePasswordAuthenticationToken){
            TenantUsernamePasswordAuthenticationToken tenantToken = (TenantUsernamePasswordAuthenticationToken) principal;
            String clientId = tenantToken.getClientId();
            String requestClientId = parameters.get(OAuth2Utils.CLIENT_ID);
            // 判断是否不同租户单点登录
            if (!requestClientId.equals(clientId)){
                Object details = tenantToken.getDetails();
                try {
                    TenantContextHolder.setTenant(requestClientId);
                    // 重新查询对应该租户的角色等信息
                    LoginAppUser user = (LoginAppUser) userDetailServiceFactory.getService(tenantToken)
                            .loadUserByUsername(tenantToken.getName());
                    tenantToken = new TenantUsernamePasswordAuthenticationToken(user,tenantToken.getCredentials(),user.getAuthorities(),requestClientId);
                    tenantToken.setDetails(details);
                    args[3] = tenantToken;
                }finally {
                    TenantContextHolder.clear();
                }
            }
        }

        return joinPoint.proceed(args);
    }
}
