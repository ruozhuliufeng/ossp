package cn.aixuxi.ossp.uaa.service;

import cn.aixuxi.ossp.uaa.entity.SysRolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

public interface SysRolePermissionService extends IService<SysRolePermission> {

    /**
     * 根据角色id集合获取权限id
     * @param roleIds 角色id集合
     * @return 权限id集合
     */
    Set<Integer> selectPermByRoleIds(List<Integer> roleIds);
}
