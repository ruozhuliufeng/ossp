package cn.aixuxi.ossp.business.user.service.impl;

import cn.aixuxi.ossp.business.user.mapper.SysMenuMapper;
import cn.aixuxi.ossp.business.user.service.ISysMenuService;
import cn.aixuxi.ossp.common.model.SysMenu;
import cn.aixuxi.ossp.common.service.impl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:26
 **/
@Service
@Slf4j
public class SysMenuServiceImpl extends SuperServiceImpl<SysMenuMapper, SysMenu>
                            implements ISysMenuService {
}
