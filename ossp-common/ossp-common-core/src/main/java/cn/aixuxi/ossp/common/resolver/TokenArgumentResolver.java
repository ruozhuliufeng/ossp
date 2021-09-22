package cn.aixuxi.ossp.common.resolver;

import cn.aixuxi.ossp.common.annotatioin.LoginUser;
import cn.aixuxi.ossp.common.constant.SecurityConstants;
import cn.aixuxi.ossp.common.feign.UserService;
import cn.aixuxi.ossp.common.model.SysRole;
import cn.aixuxi.ossp.common.model.SysUser;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.collections.list.AbstractLinkedList;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Token转化为SysUser
 * @author ruozhuliufeng
 * @date 2021-09-01
 */
@Slf4j
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {
    private UserService userService;
    public TokenArgumentResolver(UserService userService){
        this.userService = userService;
    }

    /**
     * 入参筛选
     * @param methodParameter 参数集合
     * @return 格式化后的参数
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(LoginUser.class) && methodParameter.getParameterType().equals(SysUser.class);
    }
    /**
     * @param methodParameter 入参集合
     * @param modelAndViewContainer model view
     * @param nativeWebRequest web相关
     * @param webDataBinderFactory 入参解析
     * @return 包装对象
     * @throws Exception 异常
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        LoginUser loginUser = methodParameter.getParameterAnnotation(LoginUser.class);
        boolean isFull = loginUser.isFull();
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String userId = request.getHeader(SecurityConstants.USER_ID_HEADER);
        String username = request.getHeader(SecurityConstants.USER_HEADER);
        String roles = request.getHeader(SecurityConstants.ROLE_HEADER);
        // 账号类型
        String accountType = request.getHeader(SecurityConstants.ACCOUNT_TYPE_HEADER);
        if (StrUtil.isBlank(username)){
            log.warn("参数有误，用户名为空！");
            return null;
        }
        SysUser user;
        if (isFull){
            user = userService.selectByUsername(username);
        }else {
            user = new SysUser();
            user.setId(Long.valueOf(userId));
            user.setUsername(username);
        }
        List<SysRole> sysRoleList = new ArrayList<>();
        Arrays.stream(roles.split(",")).forEach(role->{
            SysRole sysRole = new SysRole();
            sysRole.setCode(role);
            sysRoleList.add(sysRole);
        });
        user.setRoles(sysRoleList);
        return user;
    }
}
