package cn.aixuxi.ossp.uaa.service.impl;

import cn.aixuxi.ossp.auth.client.util.AuthUtils;
import cn.aixuxi.ossp.common.constant.SecurityConstants;
import cn.aixuxi.ossp.uaa.service.OsspUserDetailsService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户Service工厂
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-22 16:46
 **/
@Slf4j
@Service
public class UserDetailServiceFactory {
    private static final String ERROR_MSG = "找不到账户类型为{}的实体类";
    @Resource
    private List<OsspUserDetailsService> userDetailsServices;

    public OsspUserDetailsService getService(Authentication authentication){
        String accoutType = AuthUtils.getAccountType(authentication);
        return this.getService(accoutType);
    }

    public OsspUserDetailsService getService(String accountType){
        if (StrUtil.isEmpty(accountType)){
            accountType = SecurityConstants.DEF_ACCOUNT_TYPE;
        }
        log.info("用户服务工厂获取账号类型为{}的用户服务",accountType);
        if (CollUtil.isNotEmpty(userDetailsServices)){
            for (OsspUserDetailsService userService:userDetailsServices){
                if (userService.supports(accountType)){
                    return userService;
                }
            }
        }
        throw new InternalAuthenticationServiceException(StrUtil.format(ERROR_MSG,accountType));
    }
}
