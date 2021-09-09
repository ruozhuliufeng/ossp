package cn.aixuxi.ossp.business.user.controller;

import cn.aixuxi.ossp.business.user.model.SysUserExcel;
import cn.aixuxi.ossp.business.user.service.ISysUserService;
import cn.aixuxi.ossp.common.annotatioin.LoginUser;
import cn.aixuxi.ossp.common.constant.CommonConstant;
import cn.aixuxi.ossp.common.log.annotation.AuditLog;
import cn.aixuxi.ossp.common.model.*;
import cn.aixuxi.ossp.common.utils.ExcelUtil;
import cn.aixuxi.ossp.search.client.client.service.IQueryService;
import cn.aixuxi.ossp.search.client.model.LogicDelDTO;
import cn.aixuxi.ossp.search.client.model.SearchDTO;
import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.multi.MultiInternalFrameUI;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户管理
 *
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
    private static final LogicDelDTO SEARCH_LOGIC_DEL_DTO = new LogicDelDTO("isDel", "否");
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IQueryService queryService;

    /**
     * 当前登录用户 LoginAppUser
     *
     * @param user 系统用户信息
     * @return LoginAppUser
     */
    @ApiOperation(value = "根据access_token查找当前用户")
    @GetMapping("/users/current")
    public Result<LoginAppUser> getLoginAppUser(@LoginUser(isFull = true) SysUser user) {
        return Result.succeed(userService.getLoginAppUser(user));
    }

    /**
     * 查询用户实体对象SysUser
     *
     * @param username 用户名
     * @return ysUser
     */
    @ApiOperation(value = "根据用户名查询用户实体")
    @Cacheable(value = "user", key = "#username")
    @GetMapping(value = "/users/name/{username}")
    public SysUser selectByUsername(@PathVariable String username) {
        return userService.selectByUsername(username);
    }

    /**
     * 查询用户登录对象 LoginAppUser
     *
     * @param username 用户名
     * @return 用户登录对象
     */
    @GetMapping(value = "/users-anon/login", params = "username")
    @ApiOperation(value = "根据用户名查找用户")
    public LoginAppUser findByUsername(String username) {
        return userService.findByUsername(username);
    }

    /**
     * 根据手机号查询用户、角色信息
     *
     * @param mobile 手机号
     * @return 用户信息
     */
    @GetMapping(value = "/users-anon/mobile", params = "mobile")
    @ApiOperation(value = "根据手机号查询用户")
    public SysUser findByMobile(String mobile) {
        return userService.findByMobile(mobile);
    }

    /**
     * 根据OpenId查询用户信息
     *
     * @param openId openId
     * @return 用户信息
     */
    @GetMapping(value = "/users-anon/openId", params = "openId")
    @ApiOperation(value = "根据OpenId查询用户")
    public SysUser findByOpenId(String openId) {
        return userService.findByOpenId(openId);
    }

    @GetMapping("/users/{id}")
    public SysUser findByUserId(@PathVariable Long id) {
        return userService.getById(id);
    }

    /**
     * 管理后台修改用户
     *
     * @param sysUser 用户信息
     */
    @PutMapping("/users")
    @CachePut(value = "user", key = "#sysUser.username", unless = "#result == null")
    @AuditLog(operation = "'更新用户:' + #sysUser")
    public void updateSysUser(@RequestBody SysUser sysUser) {
        userService.updateById(sysUser);
    }

    /**
     * 管理后台给用户分配角色
     *
     * @param id      用户id
     * @param roleIds 角色id集合
     */
    @PostMapping("/users/{id}/roles")
    public void setRoleToUser(@PathVariable Long id, @RequestBody Set<Long> roleIds) {
        userService.setRoleToUser(id, roleIds);
    }

    /**
     * 获取用户的角色
     *
     * @param id 用户id
     * @return 角色集合
     */
    @GetMapping("/users/{id}/roles")
    public List<SysRole> findRolesByUserId(Long id) {
        return userService.findRolesByUserId(id);
    }

    /**
     * 用户查询
     *
     * @param params 查询参数
     * @return 用户列表
     */
    @ApiOperation(value = "用户查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer")
    })
    @GetMapping("/users")
    public PageResult<SysUser> findUsers(@RequestParam Map<String, Object> params) {
        return userService.findUsers(params);
    }

    @ApiOperation(value = "修改用户状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "enabled", value = "是否启用", required = true, dataType = "Boolean"),
    })
    @GetMapping("/users/updateEnabled")
    public Result updateEnabled(Map<String, Object> params) {
        Long id = MapUtils.getLong(params, "id");
        if (checkAdmin(id)) {
            return Result.failed(ADMIN_CHANGE_MSG);
        }
        return userService.updateEnables(params);
    }

    /**
     * 管理后台，给用户重置密码
     *
     * @param id 用户id
     * @return Result
     */
    @PutMapping(value = "/user/{id}/password")
    @AuditLog(operation = "'重置用户密码:'+#id")
    public Result resetPassword(Long id) {
        if (checkAdmin(id)) {
            return Result.failed(ADMIN_CHANGE_MSG);
        }
        userService.updatePassword(id, null, null);
        return Result.succeed("重置成功");
    }

    /**
     * 用户自己修改密码
     *
     * @param sysUser 用户信息
     * @return Result
     */
    @PutMapping(value = "/users/password")
    public Result resetPassword(SysUser sysUser) {
        if (checkAdmin(sysUser.getId())) {
            return Result.failed(ADMIN_CHANGE_MSG);
        }
        userService.updatePassword(sysUser.getId(), sysUser.getOldPassword(), sysUser.getNewPassword());
        return Result.succeed("重置成功");
    }

    /**
     * 新增or更新
     *
     * @param sysUser 用户信息
     * @return Result
     * @throws Exception 异常
     */
    @CacheEvict(value = "user", key = "#sysUser.username")
    @PostMapping("/users/saveOrUpdate")
    @AuditLog(operation = "'新增或更新用户:'+#sysUser.username")
    public Result saveOrUpdate(SysUser sysUser) throws Exception {
        return userService.saveOrUpdateUser(sysUser);
    }

    /**
     * 导出Excel
     * @param params 导出参数
     * @param response 返回
     * @throws IOException 导出异常
     */
    @PostMapping("/users/export")
    public void exportUser(@RequestParam Map<String,Object> params, HttpServletResponse response) throws IOException {
        List<SysUserExcel> result = userService.findAllUsers(params);
        // 导出操作
        ExcelUtil.exportExcel(result,null,"用户",SysUserExcel.class,"user",response);
    }

    @PostMapping(value = "/users/import")
    public Result importExcel(@RequestParam("file") MultipartFile excel) throws Exception{
        int rowNum = 0;
        if (!excel.isEmpty()){
            List<SysUserExcel> list = ExcelUtil.importExcel(excel,0,1,SysUserExcel.class);
            rowNum = list.size();
            if (rowNum>0){
                List<SysUser> users = new ArrayList<>(rowNum);
                list.forEach(u->{
                    SysUser user = new SysUser();
                    BeanUtil.copyProperties(u,user);
                    user.setPassword(CommonConstant.DEF_USER_PASSWORD);
                    user.setType(UserType.BACKEND.name());
                    users.add(user);
                });
                userService.saveBatch(users);
            }
        }
        return Result.succeed("导入数据成功，一共【"+rowNum+"】行");
    }

    @ApiOperation(value = "用户全文搜索列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "分页起始位置",required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "limit",value = "分页结束位置",required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "queryStr",value = "搜索关键字",dataType = "String")
    })
    @GetMapping("/users/search")
    public PageResult<JsonNode> search(SearchDTO searchDTO){
        searchDTO.setIsHighlighter(true);
        searchDTO.setSortCol("createTime");
        return queryService.strQuery("sys_user",searchDTO,SEARCH_LOGIC_DEL_DTO);
    }

    /**
     * 判断是否超级管理员
     *
     * @param id 用户id
     * @return 是否超级管理员
     */
    private boolean checkAdmin(long id) {
        return id == 1L;
    }
}
