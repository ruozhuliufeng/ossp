package cn.aixuxi.ossp.search.client.client.service.impl;

import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.search.client.client.feign.AggregationService;
import cn.aixuxi.ossp.search.client.client.feign.SearchService;
import cn.aixuxi.ossp.search.client.client.service.IQueryService;
import cn.aixuxi.ossp.search.client.model.LogicDelDTO;
import cn.aixuxi.ossp.search.client.model.SearchDTO;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 搜索客户端Service
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 11:17
 **/
public class QueryServiceImpl implements IQueryService {

    @Resource
    private SearchService searchService;
    @Resource
    private AggregationService aggregationService;
    /**
     * 查询文档列表
     *
     * @param indexName 索引名
     * @param searchDTO 搜索DTO
     * @return PageResult
     */
    @Override
    public PageResult<JsonNode> strQuery(String indexName, SearchDTO searchDTO) {
        return strQuery(indexName,searchDTO,null);
    }

    /**
     * 查询文档列表
     *
     * @param indexName   索引名
     * @param searchDTO   搜索DTO
     * @param logicDelDTO 逻辑删除DTO
     * @return PageResult
     */
    @Override
    public PageResult<JsonNode> strQuery(String indexName, SearchDTO searchDTO, LogicDelDTO logicDelDTO) {
        setLogicDelQueryStr(searchDTO,logicDelDTO);
        return searchService.strQuery(indexName,searchDTO);
    }

    /**
     * 拼装逻辑删除的条件
     * @param searchDTO 搜索DTO
     * @param logicDelDTO 逻辑删除DTO
     */
    private void setLogicDelQueryStr(SearchDTO searchDTO, LogicDelDTO logicDelDTO) {
        if(logicDelDTO != null
            && StrUtil.isNotEmpty(logicDelDTO.getLogicDelFiels())
            && StrUtil.isNotEmpty(logicDelDTO.getLogicnotDelValue())){
            String result;
            // 搜索条件
            String queryStr = searchDTO.getQueryStr();
            // 拼凑逻辑删除的条件
            String logicStr = logicDelDTO.getLogicDelFiels() + ":" + logicDelDTO.getLogicnotDelValue();
            if (StrUtil.isNotEmpty(queryStr)){
                result = "(" + queryStr + ") AND " + logicStr;
            }else {
                result = logicStr;
            }
            searchDTO.setQueryStr(result);
        }
    }

    /**
     * 访问统计聚和查询
     *
     * @param indexName 索引名
     * @param routing   ES的路由
     * @return Map
     */
    @Override
    public Map<String, Object> requestStatAgg(String indexName, String routing) {
        return aggregationService.requestStatAgg(indexName,routing);
    }
}
