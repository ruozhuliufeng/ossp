package cn.aixuxi.ossp.search.server.search.service;

import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.search.client.model.SearchDTO;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * 搜索
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 14:37
 **/
public interface ISearchService {

    /**
     * StringQuery通用搜索
     * @param indexName 索引名
     * @param searchDTO 搜索DTO
     * @return PageResult
     * @throws IOException 异常
     */
    PageResult<JsonNode> strQuery(String indexName, SearchDTO searchDTO) throws IOException;
}
