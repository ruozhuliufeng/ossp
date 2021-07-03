package cn.aixuxi.ossp.uaa.service.impl;

import cn.aixuxi.auth.server.dto.SysPermissionDTO;
import cn.aixuxi.ossp.uaa.entity.SysPermission;
import cn.aixuxi.ossp.uaa.entity.SysUser;
import cn.aixuxi.ossp.uaa.mapper.SysPermissionMapper;
import cn.aixuxi.ossp.uaa.service.SysPermissionService;
import cn.aixuxi.ossp.uaa.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 类描述 权限服务类，继承MP通用服务实现类，实现基础增删改查
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021/4/14 21:35
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 功能描述: 获取当前登录用户的菜单栏
     *
     * @return : java.util.List<cn.aixuxi.auth.server.dto.SysPermissionDTO>
     * @author : ruozhuliufeng
     * @date : 2021/6/14 21:04
     */
    @Override
    public List<SysPermissionDTO> getCurrentUserNav() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser user = sysUserService.selectByUsername(username);
        Set<Integer> permIds = sysUserService.getNavIds(user.getId());
        List<SysPermission> permissions = this.listByIds(permIds);
        // 转树形结构
        List<SysPermission> permissionList = buildTreePermission(permissions);
        // 实体转DTO
        return convert(permissionList);
    }

    /**
     * 功能描述: 树形结构
     *
     * @return : java.util.List<cn.aixuxi.ossp.uaa.entity.SysPermission>
     * @author : ruozhuliufeng
     * @date : 2021/6/14 23:53
     */
    @Override
    public List<SysPermission> tree() {
        // 获取所有权限信息
        List<SysPermission> sysPermissions = this.list(new QueryWrapper<SysPermission>().orderByAsc("order_num"));
        // 转成树形结构
        return buildTreePermission(sysPermissions);
    }


    /**
     * 功能描述: 实体转DTO
     * @param permissionList 实体数组
     * @return : DTO
     * @author : ruozhuliufeng
     * @date : 2021/6/14 22:17
     */
    private List<SysPermissionDTO> convert(List<SysPermission> permissionList) {
        List<SysPermissionDTO> permissionDTOS = new ArrayList<>();
        permissionList.forEach(m->{
            SysPermissionDTO dto = new SysPermissionDTO();
            dto.setId(m.getId());
            dto.setName(m.getPermission());
            dto.setTitle(m.getName());
            dto.setComponent(m.getComponent());
            dto.setPath(m.getPath());
            if (m.getChildren().size()>0){
                // 子节点调用当前方法进行再次转换
                dto.setChildren(convert(m.getChildren()));
            }
            permissionDTOS.add(dto);
        });
        return permissionDTOS;
    }


    private List<SysPermission> buildTreePermission(List<SysPermission> permissions) {
        List<SysPermission> finalPermission = new ArrayList<>();
        // 先各自寻找到各自的孩子
        for (SysPermission permission:permissions) {
            for (SysPermission p:permissions) {
                if (permission.getId().equals(p.getParentId())){
                    permission.getChildren().add(p);
                }
            }
            // 提取出父节点
            if (permission.getParentId() == 0){
                finalPermission.add(permission);
            }
        }
        return finalPermission;
    }
}
