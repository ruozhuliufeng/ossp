package cn.aixuxi.ossp.uaa.service;

import cn.aixuxi.auth.server.dto.SysPermissionDTO;
import cn.aixuxi.ossp.uaa.entity.SysPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysPermissionService extends IService<SysPermission> {

    /**
     * 功能描述: 获取当前登录用户的菜单栏
     * @return : java.util.List<cn.aixuxi.auth.server.dto.SysPermissionDTO>
     * @author : ruozhuliufeng
     * @date : 2021/6/14 21:04
     */
    List<SysPermissionDTO> getCurrentUserNav();

    /**
     * 功能描述: 树形结构
     * @return : java.util.List<cn.aixuxi.ossp.uaa.entity.SysPermission>
     * @author : ruozhuliufeng
     * @date : 2021/6/14 23:53
     */
    List<SysPermission> tree();
}
