package cn.aixuxi.ossp.uaa.tenant;

import cn.aixuxi.ossp.auth.client.token.TenantUsernamePasswordAuthenticationToken;
import cn.aixuxi.ossp.common.context.TenantContextHolder;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 替换UsernamePasswordAuthenticationFilter，增加租户id
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 14:04
 **/
@Setter
public class TenantUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private boolean postOnly = true;

    public TenantUsernamePasswordAuthenticationFilter(){
        super();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")){
            throw new AuthenticationServiceException(
                    "授权方法仅支持POST方法，不支持"+request.getMethod());
        }
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String clientId = TenantContextHolder.getTenant();
        if (username == null){
            username = "";
        }
        if (password == null){
            password = "";
        }
        username = username.trim();

        TenantUsernamePasswordAuthenticationToken authRequest = new TenantUsernamePasswordAuthenticationToken(username,password,clientId);
        setDetails(request,authRequest);
        return getAuthenticationManager().authenticate(authRequest);
    }
}
