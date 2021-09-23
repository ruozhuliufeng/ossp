package cn.aixuxi.ossp.uaa.password;

import cn.aixuxi.ossp.uaa.service.impl.UserDetailServiceFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

/**
 * 扩展用户密码provider
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-22 16:13
 **/
@Getter
@Setter
public class PasswordAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private UserDetailServiceFactory userDetailServiceFactory;
    private static final String USER_NOT_FOUND_PASSROD = "用户未找到密码！";
    private PasswordEncoder passwordEncoder;

    private volatile String userNotFoundEncodedPassword;

    private UserDetailsPasswordService userDetailsPasswordService;

    public PasswordAuthenticationProvider() {
        setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }



    @Override
    @SuppressWarnings("deprecation")
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        if (authentication.getCredentials() == null){
            logger.debug("身份认证失败！未提供凭据");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "凭据有误"));
        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())){
            logger.debug("身份认证失败！提供密码与已有密码不匹配");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "凭据有误"));
        }
    }

    @Override
    protected void doAfterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailServiceFactory,"用户服务工厂类必须被注入");
    }

    @Override
    protected UserDetails retrieveUser(String username,
                                       UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        prepareTimingAttackProtection();
        try{
            UserDetails loadedUser = userDetailServiceFactory.getService(authentication).loadUserByUsername(username);
            if (loadedUser == null){
                throw new InternalAuthenticationServiceException("用户详情服务返回为空！");
            }
            return loadedUser;
        }catch (UsernameNotFoundException exception){
            mitigateAgainstTimingAttack(authentication);
            throw exception;
        }catch (InternalAuthenticationServiceException exception){
            throw exception;
        }catch (Exception exception){
            throw new InternalAuthenticationServiceException(exception.getMessage(),exception);
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal,
                                                         Authentication authentication,
                                                         UserDetails user) {
        boolean upgradeEncoding = this.userDetailsPasswordService != null
                && this.passwordEncoder.upgradeEncoding(user.getPassword());
        if (upgradeEncoding){
            String presentedPassword = authentication.getCredentials().toString();
            String newPassword = this.passwordEncoder.encode(presentedPassword);
            user = this.userDetailsPasswordService.updatePassword(user,newPassword);
        }
        return super.createSuccessAuthentication(principal, authentication, user);
    }

    private void prepareTimingAttackProtection(){
        if (this.userNotFoundEncodedPassword == null){
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSROD);
        }
    }

    private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication){
        if (authentication.getCredentials() != null){
            String presentedPasword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPasword,this.userNotFoundEncodedPassword);
        }
    }
    /**
     * 设置用于编码和验证密码的 PasswordEncoder 实例。 如果未设置，
     * 将使用PasswordEncoderFactories.createDelegatingPasswordEncoder()比较
     * PasswordEncoderFactories.createDelegatingPasswordEncoder()
     * @param passwordEncoder 必须是PasswordEncode实例之一
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder,"密码加密工具不能为空");
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }
}
