package cn.aixuxi.ossp.search.client.client.feign.fallback;

import cn.aixuxi.ossp.search.client.client.feign.AggregationService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * SearchService 降级工场
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 11:17
 **/
@Component
public class AggregationServiceFallbackFactory implements FallbackFactory<AggregationService> {
    @Override
    public AggregationService create(Throwable throwable) {
        return null;
    }
}
