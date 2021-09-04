package cn.aixuxi.ossp.search.client.client.feign;

import cn.aixuxi.ossp.common.constant.ServiceNameConstants;
import cn.aixuxi.ossp.search.client.client.feign.fallback.SearchServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 11:16
 **/
@FeignClient(name = ServiceNameConstants.SEARCH_SERAVICE,fallbackFactory = SearchServiceFallbackFactory.class,decode404 = true)
public interface AggregationService {
    /**
     * 查询文档列表
     * @param indexName 索引名
     * @param routing es的路由
     * @return
     */
    @GetMapping(value = "/agg/requestStat/{indexName}/{routing}")
    Map<String,Object> requestStatAgg(@PathVariable("indexName") String indexName,@PathVariable("routing") String routing);
}
