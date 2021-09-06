package cn.aixuxi.ossp.auth.client.token;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 增加租户id，解决不同租户单点登录时角色没有变化
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 16:26
 **/
public class TenantUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private static final long serialVersionUID = -5638287853803374687L;

    /**
     * 租户id
     */
    @Getter
    private final String clientId;

    public TenantUsernamePasswordAuthenticationToken(Object principal, Object credentials,String clientId) {
        super(principal, credentials);
        this.clientId = clientId;
    }

    public TenantUsernamePasswordAuthenticationToken(Object principal,
                                                     Object credentials, Collection<? extends GrantedAuthority> authorities,
                                                     String clientId) {
        super(principal, credentials, authorities);
        this.clientId = clientId;
    }
}
