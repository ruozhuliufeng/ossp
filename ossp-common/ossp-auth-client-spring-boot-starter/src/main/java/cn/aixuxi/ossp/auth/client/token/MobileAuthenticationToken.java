package cn.aixuxi.ossp.auth.client.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import javax.management.MalformedObjectNameException;
import java.util.Collection;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 16:29
 **/
public class MobileAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final Object principal;
    private Object credentials;

    public MobileAuthenticationToken(String mobile, String password) {
        super(null);
        this.principal = mobile;
        this.credentials = password;
        setAuthenticated(false);
    }


    public MobileAuthenticationToken(Object principal, Object credentials,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "无法将此令牌设置为受信任 - 使用采用 GrantedAuthority 列表的构造函数代替"
            );
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
