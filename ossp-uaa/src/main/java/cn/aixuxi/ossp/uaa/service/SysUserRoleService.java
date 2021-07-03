package cn.aixuxi.ossp.uaa.service;

import cn.aixuxi.ossp.uaa.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysUserRoleService extends IService<SysUserRole> {
    /**
     * 根据用户id获取角色id列表
     * @param id 用户id
     * @return 角色id集合
     */
    List<Integer> selectRoleByUserId(Integer id);
}
