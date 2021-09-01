package cn.aixuxi.ossp.common.config;

import cn.aixuxi.ossp.common.utils.PwdEncoderUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码加密配置类
 * @author ruozhuliufeng
 * @date 2021-09-01
 */
public class DefaultPasswordConfig {

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder(){
        return PwdEncoderUtil.getDelegatingPasswordEncoder("bcrypt");
    }
}
