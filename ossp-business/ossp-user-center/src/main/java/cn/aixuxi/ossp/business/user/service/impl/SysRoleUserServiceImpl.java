package cn.aixuxi.ossp.business.user.service.impl;

import cn.aixuxi.ossp.business.user.mapper.SysUserRoleMapper;
import cn.aixuxi.ossp.business.user.model.SysRoleUser;
import cn.aixuxi.ossp.business.user.service.ISysRoleUserService;
import cn.aixuxi.ossp.common.model.SysRole;
import cn.aixuxi.ossp.common.service.impl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:30
 **/
@Slf4j
@Service
public class SysRoleUserServiceImpl extends SuperServiceImpl<SysUserRoleMapper, SysRoleUser>
                                    implements ISysRoleUserService {
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    /**
     * 保存用户-角色信息
     *
     * @param userId 用户id
     * @param roleId 角色id
     * @return 影响数
     */
    @Override
    public int saveUserRoles(Long userId, Long roleId) {
        return sysUserRoleMapper.saveUserRoles(userId,roleId);
    }

    /**
     * 删除用户-角色信息
     *
     * @param userId 用户id
     * @param roleId 角色id
     * @return 影响数
     */
    @Override
    public int deleteUserRole(Long userId, Long roleId) {
        return sysUserRoleMapper.deleteUserRole(userId,roleId);
    }

    /**
     * 根据用户id获取角色信息
     *
     * @param userId 用户id
     * @return 角色集合
     */
    @Override
    public List<SysRole> findRolesByUserId(Long userId) {
        return sysUserRoleMapper.findRolesByUserId(userId);
    }

    /**
     * 根据用户id集合获取角色集合
     *
     * @param userIds 用户id集合
     * @return 角色集合
     */
    @Override
    public List<SysRole> findRolesByUserIds(List<Long> userIds) {
        return sysUserRoleMapper.findRolesByUserIds(userIds);
    }
}
