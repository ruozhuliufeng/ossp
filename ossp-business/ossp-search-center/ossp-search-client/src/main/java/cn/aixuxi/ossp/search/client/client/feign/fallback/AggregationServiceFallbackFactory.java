package cn.aixuxi.ossp.search.client.client.feign.fallback;

import cn.aixuxi.ossp.search.client.client.feign.AggregationService;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * SearchService 降级工场
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 11:17
 **/
@Component
@Slf4j
public class AggregationServiceFallbackFactory implements FallbackFactory<AggregationService> {
    @Override
    public AggregationService create(Throwable throwable) {
        return ((indexName, routing) -> {
            log.error("通过索引{}搜索异常：{}", indexName,throwable);
            return MapUtil.newHashMap();
        });
    }
}
