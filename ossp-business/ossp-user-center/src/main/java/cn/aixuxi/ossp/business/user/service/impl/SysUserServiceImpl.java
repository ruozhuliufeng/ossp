package cn.aixuxi.ossp.business.user.service.impl;

import cn.aixuxi.ossp.business.user.mapper.SysUserMapper;
import cn.aixuxi.ossp.business.user.model.SysUserExcel;
import cn.aixuxi.ossp.business.user.service.ISysUserService;
import cn.aixuxi.ossp.common.model.*;
import cn.aixuxi.ossp.common.service.impl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:30
 **/
@Slf4j
@Service
public class SysUserServiceImpl extends SuperServiceImpl<SysUserMapper, SysUser>
                                implements ISysUserService {
    /**
     * 获取UserDetails对象
     *
     * @param username 用户名
     * @return LoginAppUser
     */
    @Override
    public LoginAppUser findByUsername(String username) {
        return null;
    }

    @Override
    public LoginAppUser findByOpenId(String openId) {
        return null;
    }

    @Override
    public LoginAppUser findByMobile(String mobile) {
        return null;
    }

    /**
     * 通过SysUser转换为LoginAppUser，同时查询赋值roles和permissions
     *
     * @param sysUser 系统用户信息
     * @return 登录用户信息
     */
    @Override
    public LoginAppUser getLoginAppUser(SysUser sysUser) {
        return null;
    }

    /**
     * 根据用户名查询用户
     *
     * @param username 用户
     * @return 系统用户信息
     */
    @Override
    public SysUser selectByUsername(String username) {
        return null;
    }

    /**
     * 根据手机号查询用户
     *
     * @param mobile 手机号
     * @return 系统用户信息
     */
    @Override
    public SysUser selectByMobile(String mobile) {
        return null;
    }

    /**
     * 根据openId查询用户
     *
     * @param openId openId
     * @return 系统用户信息
     */
    @Override
    public SysUser selectOpenId(String openId) {
        return null;
    }

    /**
     * 用户分配角色
     *
     * @param id      用户id
     * @param roleIds 角色id集合
     */
    @Override
    public void setRoleToUser(Long id, Set<Long> roleIds) {

    }

    /**
     * 更新用户密码
     *
     * @param id          用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return Result
     */
    @Override
    public Result updatePassword(Long id, String oldPassword, String newPassword) {
        return null;
    }

    /**
     * 获取用户列表
     *
     * @param params 查询参数
     * @return 用户列表
     */
    @Override
    public PageResult<SysUser> findUsers(Map<String, Object> params) {
        return null;
    }

    /**
     * 根据用户id获取角色列表
     *
     * @param userId 用户id
     * @return 角色列表
     */
    @Override
    public List<SysRole> findRolesByUserId(Long userId) {
        return null;
    }

    /**
     * 用户状态变更
     *
     * @param params 参数
     * @return Result
     */
    @Override
    public Result updateEnables(Map<String, Object> params) {
        return null;
    }

    /**
     * 查询所有用户
     *
     * @param params 参数
     * @return 导出用户集合
     */
    @Override
    public List<SysUserExcel> findAllUser(Map<String, Object> params) {
        return null;
    }

    /**
     * 保存或更新用户信息
     *
     * @param sysUser 用户信息
     * @return Result
     * @throws Exception 异常
     */
    @Override
    public Result saveOrUpdateUser(SysUser sysUser) throws Exception {
        return null;
    }

    /**
     * 删除用户信息
     *
     * @param id 用户id
     * @return
     */
    @Override
    public boolean delUser(Long id) {
        return false;
    }
}
