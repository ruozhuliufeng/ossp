package cn.aixuxi.ossp.common.feign.fallback;

import cn.aixuxi.ossp.common.feign.UserService;
import cn.aixuxi.ossp.common.model.LoginAppUser;
import cn.aixuxi.ossp.common.model.SysUser;
import org.springframework.cloud.openfeign.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * UserService降级工厂
 * @author ruozhuliufeng
 * @date 2021-09-01
 */
@Slf4j
public class UserServiceFallbackFactory implements FallbackFactory<UserService> {

    @Override
    public UserService create(Throwable throwable) {
        return new UserService() {
            @Override
            public SysUser selectByUsername(String username) {
                log.error("通过用户名查询用户异常：{}",username,throwable);
                return new SysUser();
            }

            @Override
            public LoginAppUser findByUsername(String username) {
                log.error("通过用户名查询用户异常：{}",username,throwable);
                return new LoginAppUser();
            }

            @Override
            public LoginAppUser findByMobile(String mobile) {
                log.error("通过手机号查询用户异常：{}",mobile,throwable);
                return new LoginAppUser();
            }

            @Override
            public LoginAppUser findByOpenId(String openId) {
                log.error("通过openId查询用户异常：{}",openId,throwable);
                return new LoginAppUser();
            }
        };
    }
}
