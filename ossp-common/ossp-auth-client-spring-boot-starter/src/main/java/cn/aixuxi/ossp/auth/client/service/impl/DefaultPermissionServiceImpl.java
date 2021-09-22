package cn.aixuxi.ossp.auth.client.service.impl;

import cn.aixuxi.ossp.auth.client.properties.SecurityPropertis;
import cn.aixuxi.ossp.auth.client.util.AuthUtils;
import cn.aixuxi.ossp.common.constant.CommonConstant;
import cn.aixuxi.ossp.common.context.TenantContextHolder;
import cn.aixuxi.ossp.common.model.SysMenu;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 请求权限判断Service
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 16:35
 **/
@Slf4j
public abstract class DefaultPermissionServiceImpl {
    @Autowired
    private SecurityPropertis propertis;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 查询当前用户拥有的资源权限
     * @param roleCodes 角色code列表，多个以','隔开
     * @return 资源权限列表
     */
    public abstract List<SysMenu> findMenuByRoleCodes(String roleCodes);

    public boolean hasPermission(Authentication authentication,String requestMethod,String requestURI){
        // 前端跨域OPTIONS请求预检放行，也可以通过前端配置代理实现
        if (HttpMethod.OPTIONS.name().equals(requestMethod)){
            return true;
        }
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            // 判断是否开启url权限认证
            if (!propertis.getAuth().getUrlPermission().getEnable()){
                return true;
            }
            // 超级管理员admin不需要认证
            String username = AuthUtils.getUsername(authentication);
            if (CommonConstant.ADMIN_USER_NAME.equals(username)){
                return true;
            }
            OAuth2Authentication auth2Authentication = (OAuth2Authentication) authentication;
            // 判断应用白名单
            if (!isNeedAuth(auth2Authentication.getOAuth2Request().getClientId())){
                return true;
            }
            // 判断不需要进行url权限认证的api，所有已登录用户都能访问的url
            for(String path:propertis.getAuth().getUrlPermission().getIgnoreUrls()){
                if (antPathMatcher.match(path,requestURI)){
                    return true;
                }
            }
            List<SimpleGrantedAuthority> grantedAuthorityList = (List<SimpleGrantedAuthority>) authentication.getAuthorities();
            if (CollectionUtil.isEmpty(grantedAuthorityList)){
                log.warn("角色列表为空:{}",authentication.getPrincipal());
                return false;
            }
            // 保存租户信息
            String clientId = auth2Authentication.getOAuth2Request().getClientId();
            TenantContextHolder.setTenant(clientId);

            String roleCodes = grantedAuthorityList.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.joining(","));
            List<SysMenu> menuList = findMenuByRoleCodes(roleCodes);
            for (SysMenu menu:menuList){
                if (StringUtils.isNotEmpty(menu.getUrl()) && antPathMatcher.match(menu.getUrl(),requestURI)){
                    return requestMethod.equalsIgnoreCase(menu.getPathMethod());
                }else {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断应用是否满足黑名单与白名单的过滤逻辑
     * @param clientId 应用id
     * @return true(需要认证),false(不需要认证)
     */
    private boolean isNeedAuth(String clientId){
        boolean result = true;
        // 白名单
        List<String> includeClientIds = propertis.getAuth().getUrlPermission().getIncludeClientIds();
        // 黑名单
        List<String> exclusiveClientIds = propertis.getAuth().getUrlPermission().getExclusiveClientIds();
        if (includeClientIds.size()>0){
            result = includeClientIds.contains(clientId);
        }else if (exclusiveClientIds.size()>0){
            result = !exclusiveClientIds.contains(clientId);
        }
        return result;
    }
}
