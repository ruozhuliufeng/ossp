package cn.aixuxi.ossp.business.user.controller;

import cn.aixuxi.ossp.business.user.service.ISysUserService;
import cn.aixuxi.ossp.common.annotatioin.LoginUser;
import cn.aixuxi.ossp.common.model.LoginAppUser;
import cn.aixuxi.ossp.common.model.Result;
import cn.aixuxi.ossp.common.model.SysUser;
import cn.aixuxi.ossp.search.client.client.service.IQueryService;
import cn.aixuxi.ossp.search.client.model.LogicDelDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:36
 **/
@Slf4j
@RestController
@Api(tags = "用户模块Api")
public class SysUserController {
    private static final String ADMIN_CHANGE_MSG = "超级管理员不给予修改";

    /**
     * 全文搜索逻辑删除DTO
     */
    private static final LogicDelDTO SEARCH_LOGIC_DEL_DTO = new LogicDelDTO("isDel","否");
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IQueryService queryService;

    /**
     * 当前登录用户 LoginAppUser
     * @param user 系统用户信息
     * @return LoginAppUser
     */
    @ApiOperation(value = "根据access_token查找当前用户")
    @GetMapping("/users/current")
    public Result<LoginAppUser> getLoginAppUser(@LoginUser(isFull = true)SysUser user){
        return Result.succeed(userService.getLoginAppUser(user));
    }

    /**
     * 查询用户实体对象SysUser
     * @param username 用户名
     * @return ysUser
     */
    @ApiOperation(value = "根据用户名查询用户实体")
    @Cacheable(value = "user",key = "#username")
    @GetMapping(value = "/users/name/{username}")
    public SysUser selectByUsername(@PathVariable String username){
        return userService.selectByUsername(username);
    }

    /**
     * 判断是否超级管理员
     * @param id 用户id
     * @return 是否超级管理员
     */
    private boolean checkAdmin(long id){
        return id == 1L;
    }
}
