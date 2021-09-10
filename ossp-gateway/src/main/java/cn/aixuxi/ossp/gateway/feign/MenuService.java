package cn.aixuxi.ossp.gateway.feign;

import cn.aixuxi.ossp.common.constant.ServiceNameConstants;
import cn.aixuxi.ossp.common.model.SysMenu;
import cn.aixuxi.ossp.gateway.feign.fallback.MenuServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-10 11:21
 **/
@FeignClient(name = ServiceNameConstants.USER_SERVICE,fallbackFactory = MenuServiceFallbackFactory.class,decode404 = true)
public interface MenuService {
    /**
     * 角色菜单列表
     * @param roleCodes 角色编码
     * @return 菜单列表
     */
    @GetMapping(value = "/menus/{roleCodes}")
    List<SysMenu> findByRoleCodes(@PathVariable("roleCodes") String roleCodes);
}
