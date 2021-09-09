package cn.aixuxi.ossp.business.user.service.impl;

import cn.aixuxi.ossp.business.user.mapper.SysRoleMenuMapper;
import cn.aixuxi.ossp.business.user.mapper.SysUserMapper;
import cn.aixuxi.ossp.business.user.model.SysRoleUser;
import cn.aixuxi.ossp.business.user.model.SysUserExcel;
import cn.aixuxi.ossp.business.user.service.ISysRoleUserService;
import cn.aixuxi.ossp.business.user.service.ISysUserService;
import cn.aixuxi.ossp.common.constant.CommonConstant;
import cn.aixuxi.ossp.common.lock.DistributedLock;
import cn.aixuxi.ossp.common.model.*;
import cn.aixuxi.ossp.common.service.impl.SuperServiceImpl;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:30
 **/
@Slf4j
@Service
public class SysUserServiceImpl extends SuperServiceImpl<SysUserMapper, SysUser>
        implements ISysUserService {
    private final static String LOCK_KEY_USERNAME = "username:";
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Resource
    private ISysRoleUserService roleUserService;
    @Resource
    private SysRoleMenuMapper roleMenuMapper;
    @Autowired
    private DistributedLock lock;

    /**
     * 获取UserDetails对象
     *
     * @param username 用户名
     * @return LoginAppUser
     */
    @Override
    public LoginAppUser findByUsername(String username) {
        SysUser sysUser = this.selectByUsername(username);
        return getLoginAppUser(sysUser);
    }

    @Override
    public LoginAppUser findByOpenId(String openId) {
        SysUser sysUser = this.selectByOpenId(openId);
        return getLoginAppUser(sysUser);
    }

    @Override
    public LoginAppUser findByMobile(String mobile) {
        SysUser sysUser = this.selectByMobile(mobile);
        return getLoginAppUser(sysUser);
    }

    /**
     * 通过SysUser转换为LoginAppUser，同时查询赋值roles和permissions
     *
     * @param sysUser 系统用户信息
     * @return 登录用户信息
     */
    @Override
    public LoginAppUser getLoginAppUser(SysUser sysUser) {
        if (sysUser != null) {
            LoginAppUser loginAppUser = new LoginAppUser();
            BeanUtils.copyProperties(sysUser, loginAppUser);
            List<SysRole> sysRoles = roleUserService.findRolesByUserId(sysUser.getId());
            // 设置角色
            loginAppUser.setRoles(sysRoles);

            if (!CollectionUtils.isEmpty(sysRoles)) {
                Set<Long> roleIds = sysRoles.parallelStream().map(BaseEntity::getId).collect(Collectors.toSet());
                List<SysMenu> menus = roleMenuMapper.findMenusByRoleIds(roleIds, CommonConstant.PERMISSION);
                if (!CollectionUtils.isEmpty(menus)) {
                    Set<String> permissions = menus.parallelStream().map(p -> p.getPath())
                            .collect(Collectors.toSet());
                    // 设置权限集合
                    loginAppUser.setPermissions(permissions);
                }
            }

            return loginAppUser;
        }
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
        List<SysUser> users = baseMapper.selectList(
                new QueryWrapper<SysUser>().eq("username", username)
        );
        return getUser(users);
    }

    /**
     * 根据手机号查询用户
     *
     * @param mobile 手机号
     * @return 系统用户信息
     */
    @Override
    public SysUser selectByMobile(String mobile) {
        List<SysUser> users = baseMapper.selectList(
                new QueryWrapper<SysUser>().eq("mobile", mobile)
        );
        return getUser(users);
    }

    /**
     * 根据openId查询用户
     *
     * @param openId openId
     * @return 系统用户信息
     */
    @Override
    public SysUser selectByOpenId(String openId) {
        List<SysUser> users = baseMapper.selectList(
                new QueryWrapper<SysUser>().eq("open_id", openId)
        );
        return getUser(users);
    }

    private SysUser getUser(List<SysUser> users) {
        SysUser user = null;
        if (users != null && !users.isEmpty()) {
            user = users.get(0);
        }
        return user;
    }

    /**
     * 用户分配角色
     *
     * @param id      用户id
     * @param roleIds 角色id集合
     */
    @Override
    public void setRoleToUser(Long id, Set<Long> roleIds) {
        SysUser sysUser = baseMapper.selectById(id);
        if (sysUser == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        roleUserService.deleteUserRole(id, null);
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<SysRoleUser> roleUsers = new ArrayList<>(roleIds.size());
            roleIds.forEach(roleId -> roleUsers.add(new SysRoleUser(id, roleId)));
            roleUserService.saveBatch(roleUsers);
        }
    }

    /**
     * 更新用户密码
     *
     * @param id          用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return Result
     */
    @Transactional
    @Override
    public Result updatePassword(Long id, String oldPassword, String newPassword) {
        SysUser sysUser = baseMapper.selectById(id);
        if (StrUtil.isNotBlank(oldPassword)) {
            if (!passwordEncoder.matches(oldPassword, sysUser.getPassword())) {
                return Result.failed("旧密码错误");
            }
        }
        if (StrUtil.isBlank(newPassword)) {
            newPassword = CommonConstant.DEF_USER_PASSWORD;
        }
        SysUser user = new SysUser();
        user.setId(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        baseMapper.updateById(user);
        return Result.succeed("修改成功");
    }

    /**
     * 获取用户列表
     *
     * @param params 查询参数
     * @return 用户列表
     */
    @Override
    public PageResult<SysUser> findUsers(Map<String, Object> params) {
        Page<SysUser> page = new Page<>(MapUtils.getInteger(params, "page"),
                MapUtils.getInteger(params, "limit"));
        List<SysUser> list = baseMapper.findList(page, params);
        long total = page.getTotal();
        if (total > 0) {
            List<Long> userIds = list.stream().map(SysUser::getId).collect(Collectors.toList());
            ;
            List<SysRole> sysRoles = roleUserService.findRolesByUserIds(userIds);
            list.forEach(u -> u.setRoles(sysRoles.stream().filter(r -> !ObjectUtils.notEqual(u.getId(), r.getUserId()))
                    .collect(Collectors.toList())));
        }
        return PageResult.<SysUser>builder().data(list).code(0).count(total).build();
    }

    /**
     * 根据用户id获取角色列表
     *
     * @param userId 用户id
     * @return 角色列表
     */
    @Override
    public List<SysRole> findRolesByUserId(Long userId) {
        return roleUserService.findRolesByUserId(userId);
    }

    /**
     * 用户状态变更
     *
     * @param params 参数
     * @return Result
     */
    @Override
    public Result updateEnables(Map<String, Object> params) {
        Long id = MapUtils.getLong(params, "id");
        Boolean enabled = MapUtils.getBoolean(params, "enabled");
        SysUser appUser = baseMapper.selectById(id);
        if (appUser == null) {
            return Result.failed("用户不存在");
        }
        appUser.setEnabled(enabled);
        appUser.setUpdateTime(new Date());
        int i = baseMapper.updateById(appUser);
        log.info("修改用户：{}", appUser);
        return i > 0 ? Result.succeed(appUser, "更新成功") : Result.failed("更新失败");
    }

    /**
     * 查询所有用户
     *
     * @param params 参数
     * @return 导出用户集合
     */
    @Override
    public List<SysUserExcel> findAllUsers(Map<String, Object> params) {
        List<SysUserExcel> sysUserExcels = new ArrayList<>();
        List<SysUser> list = baseMapper.findList(new Page<>(1, -1), params);
        for (SysUser user : list) {
            SysUserExcel sysUserExcel = new SysUserExcel();
            BeanUtils.copyProperties(user, sysUserExcel);
            sysUserExcels.add(sysUserExcel);
        }
        return sysUserExcels;
    }

    /**
     * 保存或更新用户信息
     *
     * @param sysUser 用户信息
     * @return Result
     * @throws Exception 异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result saveOrUpdateUser(SysUser sysUser) throws Exception {
        if (sysUser.getId() == null) {
            if (StringUtils.isBlank(sysUser.getType())) {
                sysUser.setType(UserType.BACKEND.name());
            }
            sysUser.setPassword(passwordEncoder.encode(CommonConstant.DEF_USER_PASSWORD));
            sysUser.setEnabled(Boolean.TRUE);
        }
        String username = sysUser.getUsername();
        boolean result = super.saveOrUpdateIdempotency(sysUser, lock,
                LOCK_KEY_USERNAME + username,
                new QueryWrapper<SysUser>().eq("username", username),
                username + "已存在");
        // 更新角色
        if (result && StrUtil.isNotEmpty(sysUser.getRoleId())) {
            roleUserService.deleteUserRole(sysUser.getId(), null);
            List roleIds = Arrays.asList(sysUser.getRoleId().split(","));
            if (!CollectionUtils.isEmpty(roleIds)) {
                List<SysRoleUser> roleUsers = new ArrayList<>();
                roleIds.forEach(roleId -> roleUsers.add(new SysRoleUser(sysUser.getId(), Long.parseLong(roleId.toString()))));
                roleUserService.saveBatch(roleUsers);
            }
        }
        return result ? Result.succeed(sysUser, "操作成功") : Result.failed();
    }

    /**
     * 删除用户信息
     *
     * @param id 用户id
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delUser(Long id) {
        roleUserService.deleteUserRole(id, null);
        return baseMapper.deleteById(id) > 0;
    }
}
