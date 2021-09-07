package cn.aixuxi.ossp.uaa.filter;

import cn.aixuxi.ossp.common.constant.SecurityConstants;
import cn.aixuxi.ossp.common.context.TenantContextHolder;
import cn.hutool.core.util.ArrayUtil;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 设置租户id过滤器
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 13:32
 **/
public class LoginProcessSetTenantFilter extends OncePerRequestFilter {

    private static final String SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";

    private RequestMatcher requestMatcher;
    public LoginProcessSetTenantFilter(){
        requestMatcher = new AntPathRequestMatcher(SecurityConstants.OAUTH_LOGIN_PRO_URL, HttpMethod.POST.name());
    }

    /**
     * 返回true表示不执行过滤器,false代表执行
     * @param request 请求
     * @return 是否过滤
     * @throws ServletException
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if (requiresAuthentication(request)){
            return false;
        }
        return true;
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        return requestMatcher.matches(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            DefaultSavedRequest savedRequest = (DefaultSavedRequest) request.getSession().getAttribute(SAVED_REQUEST);
            if (savedRequest != null){
                String[] clientIds = savedRequest.getParameterValues("client_id");
                if (ArrayUtil.isNotEmpty(clientIds)){
                    // 保存租户id
                    TenantContextHolder.setTenant(clientIds[0]);
                }
                filterChain.doFilter(request,response);
            }
        }finally {
            TenantContextHolder.clear();
        }
    }
}
