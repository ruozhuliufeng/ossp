package cn.aixuxi.ossp.gateway.feign;

import cn.aixuxi.ossp.common.model.SysMenu;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;

/**
 * 异步Menu服务
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-18 9:39
 **/
@Component
public class AsynMenuService {
    @Lazy
    @Resource
    MenuService menuService;

    @Async
    public Future<List<SysMenu>> findByRoleCodes(String roleCodes){
        List<SysMenu> result = menuService.findByRoleCodes(roleCodes);
        return new AsyncResult<>(result);
    }
}
