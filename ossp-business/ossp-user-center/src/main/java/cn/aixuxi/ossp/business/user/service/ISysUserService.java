package cn.aixuxi.ossp.business.user.service;

import cn.aixuxi.ossp.business.user.model.SysUserExcel;
import cn.aixuxi.ossp.common.model.*;
import cn.aixuxi.ossp.common.service.ISuperService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:23
 **/
public interface ISysUserService extends ISuperService<SysUser> {

    /**
     * 获取UserDetails对象
     *
     * @param username 用户名
     * @return LoginAppUser
     */
    LoginAppUser findByUsername(String username);

    LoginAppUser findByOpenId(String openId);

    LoginAppUser findByMobile(String mobile);

    /**
     * 通过SysUser转换为LoginAppUser，同时查询赋值roles和permissions
     *
     * @param sysUser 系统用户信息
     * @return 登录用户信息
     */
    LoginAppUser getLoginAppUser(SysUser sysUser);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户
     * @return 系统用户信息
     */
    SysUser selectByUsername(String username);

    /**
     * 根据手机号查询用户
     *
     * @param mobile 手机号
     * @return 系统用户信息
     */
    SysUser selectByMobile(String mobile);

    /**
     * 根据openId查询用户
     *
     * @param openId openId
     * @return 系统用户信息
     */
    SysUser selectByOpenId(String openId);

    /**
     * 用户分配角色
     *
     * @param id      用户id
     * @param roleIds 角色id集合
     */
    void setRoleToUser(Long id, Set<Long> roleIds);

    /**
     * 更新用户密码
     *
     * @param id          用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return Result
     */
    Result updatePassword(Long id, String oldPassword, String newPassword);

    /**
     * 获取用户列表
     *
     * @param params 查询参数
     * @return 用户列表
     */
    PageResult<SysUser> findUsers(Map<String, Object> params);

    /**
     * 根据用户id获取角色列表
     *
     * @param userId 用户id
     * @return 角色列表
     */
    List<SysRole> findRolesByUserId(Long userId);

    /**
     * 用户状态变更
     *
     * @param params 参数
     * @return Result
     */
    Result updateEnables(Map<String, Object> params);

    /**
     * 查询所有用户
     *
     * @param params 参数
     * @return 导出用户集合
     */
    List<SysUserExcel> findAllUsers(Map<String, Object> params);

    /**
     * 保存或更新用户信息
     *
     * @param sysUser 用户信息
     * @return Result
     * @throws Exception 异常
     */
    Result saveOrUpdateUser(SysUser sysUser) throws Exception;

    /**
     * 删除用户信息
     * @param id 用户id
     * @return
     */
    boolean delUser(Long id);
}
