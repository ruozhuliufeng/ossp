package cn.aixuxi.ossp.gateway.feign.fallback;

import cn.aixuxi.ossp.gateway.feign.MenuService;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * MenuService降级工场
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-10 11:23
 **/
@Slf4j
@Component
public class MenuServiceFallbackFactory implements FallbackFactory<MenuService> {
    @Override
    public MenuService create(Throwable throwable) {
        return roleCodes -> {
            log.error("调用findByRoleCodes异常：{}",roleCodes,throwable);
            return CollectionUtil.newArrayList();
        };
    }
}
