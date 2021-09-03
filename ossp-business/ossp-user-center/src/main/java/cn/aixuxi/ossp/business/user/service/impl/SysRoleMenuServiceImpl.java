package cn.aixuxi.ossp.business.user.service.impl;

import cn.aixuxi.ossp.business.user.mapper.SysRoleMapper;
import cn.aixuxi.ossp.business.user.mapper.SysRoleMenuMapper;
import cn.aixuxi.ossp.business.user.model.SysRoleMenu;
import cn.aixuxi.ossp.business.user.service.ISysRoleMenuService;
import cn.aixuxi.ossp.common.model.SysRole;
import cn.aixuxi.ossp.common.service.impl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:28
 **/
@Service
@Slf4j
public class SysRoleMenuServiceImpl extends SuperServiceImpl<SysRoleMenuMapper, SysRoleMenu>
                                    implements ISysRoleMenuService {
}
