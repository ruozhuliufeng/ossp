package cn.aixuxi.ossp.search.client.client.feign;

import cn.aixuxi.ossp.common.constant.ServiceNameConstants;
import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.search.client.client.feign.fallback.SearchServiceFallbackFactory;
import cn.aixuxi.ossp.search.client.model.SearchDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.PostConstruct;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 11:16
 **/
@FeignClient(name = ServiceNameConstants.SEARCH_SERAVICE,fallbackFactory = SearchServiceFallbackFactory.class,decode404 = true)
public interface SearchService {

    /**
     * 查询文档列表
     * @param indexName 索引名
     * @param searchDTO 搜索DTO
     * @return PageResult
     */
    @PostMapping(value = "/search/{indexName}")
    PageResult<JsonNode> strQuery(@PathVariable("indexName") String indexName, @RequestBody SearchDTO searchDTO);
}
