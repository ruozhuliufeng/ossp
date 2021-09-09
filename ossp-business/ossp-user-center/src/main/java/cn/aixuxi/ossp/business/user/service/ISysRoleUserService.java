package cn.aixuxi.ossp.business.user.service;

import cn.aixuxi.ossp.business.user.model.SysRoleUser;
import cn.aixuxi.ossp.common.model.SysRole;
import cn.aixuxi.ossp.common.service.ISuperService;

import java.util.List;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:22
 **/
public interface ISysRoleUserService extends ISuperService<SysRoleUser> {

    /**
     * 保存用户-角色信息
     * @param userId 用户id
     * @param roleId 角色id
     * @return 影响数
     */
    int saveUserRoles(Long userId,Long roleId);

    /**
     * 删除用户-角色信息
     * @param userId 用户id
     * @param roleId 角色id
     * @return 影响数
     */
    int deleteUserRole(Long userId,Long roleId);

    /**
     * 根据用户id获取角色信息
     * @param userId 用户id
     * @return 角色集合
     */
    List<SysRole> findRolesByUserId(Long userId);

    /**
     * 根据用户id集合获取角色集合
     * @param userIds 用户id集合
     * @return 角色集合
     */
    List<SysRole> findRolesByUserIds(List<Long> userIds);
}
