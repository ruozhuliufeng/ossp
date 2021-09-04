package cn.aixuxi.ossp.search.client.client.feign.fallback;

import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.search.client.client.feign.SearchService;
import com.fasterxml.jackson.databind.JsonNode;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * SearchService降级工场
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 11:17
 **/
@Slf4j
public class SearchServiceFallbackFactory implements FallbackFactory<SearchService> {
    @Override
    public SearchService create(Throwable throwable) {
        return (indexName, searchDTO) -> {
            log.error("通过索引{}搜索异常：{}",indexName,throwable);
            return PageResult.<JsonNode>builder().build();
        };
    }
}
