package cn.aixuxi.ossp.search.server.search.service;

import java.io.IOException;
import java.util.Map;

/**
 * 聚和查询
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 14:39
 **/
public interface IAggregationService {
    /**
     * 访问统计聚和查询
     * @param indexName 索引名
     * @param routing ES路由
     * @return Map
     */
    Map<String,Object> requestStatAgg(String indexName,String routing) throws IOException;
}
