package cn.aixuxi.ossp.uaa.service.impl;

import cn.aixuxi.ossp.uaa.entity.SysRolePermission;
import cn.aixuxi.ossp.uaa.mapper.SysRolePermissionMapper;
import cn.aixuxi.ossp.uaa.service.SysRolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 类描述 角色-权限服务类，继承MP通用服务实现类，实现基础增删改查
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021/4/14 21:35
 */
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements SysRolePermissionService {

    @Override
    public Set<Integer> selectPermByRoleIds(List<Integer> roleIds) {
        Set<Integer> set = new HashSet<>();
        for (Integer roleId:roleIds){
            List<SysRolePermission> rolePermissions = this.list(new QueryWrapper<SysRolePermission>().eq("role_id", roleId));
            for (SysRolePermission rolePerm:rolePermissions) {
                set.add(rolePerm.getPermissionId());
            }
        }
        return set;
    }
}
