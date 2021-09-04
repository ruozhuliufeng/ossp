package cn.aixuxi.ossp.search.client.client.service;

import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.search.client.model.LogicDelDTO;
import cn.aixuxi.ossp.search.client.model.SearchDTO;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

/**
 * 搜索客户端接口
 * @author ruozhuliufeng
 * @date 2021-09-04
 */
public interface IQueryService {

    /**
     * 查询文档列表
     * @param indexName 索引名
     * @param searchDTO 搜索DTO
     * @return PageResult
     */
    PageResult<JsonNode> strQuery(String indexName, SearchDTO searchDTO);

    /**
     * 查询文档列表
     * @param indexName 索引名
     * @param searchDTO 搜索DTO
     * @param logicDelDTO 逻辑删除DTO
     * @return PageResult
     */
    PageResult<JsonNode> strQuery(String indexName, SearchDTO searchDTO, LogicDelDTO logicDelDTO);

    /**
     * 访问统计聚和查询
     * @param indexName 索引名
     * @param routing ES的路由
     * @return Map
     */
    Map<String,Object> requestStatAgg(String indexName,String routing);
}
