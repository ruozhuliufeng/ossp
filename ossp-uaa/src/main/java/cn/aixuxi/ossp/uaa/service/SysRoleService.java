package cn.aixuxi.ossp.uaa.service;

import cn.aixuxi.ossp.uaa.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    /**
     * 功能描述: 根据用户id获取角色id列表
     * @param id 用户id
     * @return : 角色id列表
     * @author : ruozhuliufeng
     * @date : 2021/6/14 20:35
     */
    List<SysRole> listRolesByUserId(Integer id);
}
