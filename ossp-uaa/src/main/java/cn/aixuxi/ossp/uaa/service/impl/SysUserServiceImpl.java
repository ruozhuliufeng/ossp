package cn.aixuxi.ossp.uaa.service.impl;

import cn.aixuxi.ossp.uaa.entity.SysPermission;
import cn.aixuxi.ossp.uaa.entity.SysRole;
import cn.aixuxi.ossp.uaa.entity.SysUser;
import cn.aixuxi.ossp.uaa.mapper.SysUserMapper;
import cn.aixuxi.ossp.uaa.service.*;
import cn.aixuxi.auth.server.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 类描述 用户实体类，继承MP通用服务实现类，实现基础增删改查
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021/4/14 21:35
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private RedisUtils redisUtils;
    
    @Override
    public SysUser selectByUsername(String username) {
        return getBaseMapper().selectOne(new QueryWrapper<SysUser>().eq("username",username));
    }

    /**
     * 根据用户id获取用户权限信息
     *
     * @param userId 用户id
     * @return 用户的权限信息
     */
    @Override
    public String getUserAuthorityInfo(Integer userId) {
        SysUser user = this.getById(userId);
        // ROLE_admin,Role_nomal,sys:user:list,.....
        String authority = "";
        if (redisUtils.hasKey("GrantedAuthority:"+user.getUsername())){
            authority = (String) redisUtils.get("GrantedAuthority:"+user.getUsername());
        }else{
            // 获取角色编码
            List<SysRole> sysRoles = sysRoleService.listRolesByUserId(userId);
            if (sysRoles.size()>0){
                String roleCodes = sysRoles.stream().map(SysRole::getRole).collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
            }
            // 获取权限编码
            Set<Integer> permIds = this.getNavIds(userId);
            if (permIds.size()>0){
                List<SysPermission> perms = sysPermissionService.listByIds(permIds);
                String perm = perms.stream().map(p->p.getPermission()).collect(Collectors.joining(","));
                authority = authority.concat(perm);
            }
            redisUtils.set("GrantedAuthority:"+user.getUsername(),authority);
        }
        return authority;
    }

    /**
     * 功能描述: 根据用户id，获取权限id
     *
     * @param id 用户id
     * @return : java.util.List<java.lang.Integer>
     * @author : ruozhuliufeng
     * @date : 2021/6/14 22:14
     */
    @Override
    public Set<Integer> getNavIds(Integer id) {
        List<Integer> roleIds = sysUserRoleService.selectRoleByUserId(id);
        Set<Integer> perm = sysRolePermissionService.selectPermByRoleIds(roleIds);
        return perm;
    }

    /**
     * 功能描述: 根据权限id清除菜单缓存
     *
     * @param id 权限id
     * @author : ruozhuliufeng
     * @date : 2021/6/14 23:48
     */
    @Override
    public void clearUserAuthorityInfoByPermId(Integer id) {
        List<SysUser> sysUsers = getBaseMapper().listByPermId(id);
        sysUsers.forEach(u->{
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }

    /**
     * 功能描述: 根据用户名清除权限缓存
     *
     * @param username 用户名
     * @author : ruozhuliufeng
     * @date : 2021/6/14 23:49
     */
    @Override
    public void clearUserAuthorityInfo(String username) {
        redisUtils.del("GrantedAuthority:"+username);
    }

    /**
     * 功能描述: 根据角色id清除权限缓存
     *
     * @param roleId 角色id
     * @author : ruozhuliufeng
     * @date : 2021/6/14 23:49
     */
    @Override
    public void clearUserAuthorityInfoByRoleId(Integer roleId) {
        List<SysUser> sysUsers = this.list(new QueryWrapper<SysUser>()
        .inSql("id","select uid from sys_user_role where role_id = "+roleId));
        sysUsers.forEach(u->{
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }

    @Override
    public List<GrantedAuthority> getUserAuthority(Integer userId){
        // 角色(ROLE_admin)、菜单权限 sys:user:list
        String authority = this.getUserAuthorityInfo(userId);

        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
