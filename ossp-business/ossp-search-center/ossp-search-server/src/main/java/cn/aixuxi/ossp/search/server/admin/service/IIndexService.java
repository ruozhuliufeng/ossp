package cn.aixuxi.ossp.search.server.admin.service;

import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.search.server.admin.model.IndexDTO;

import java.io.IOException;
import java.util.Map;

/**
 * 索引服务
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 14:05
 **/
public interface IIndexService {

    /**
     * 创建索引
     * @param indexDTO 索引数据
     * @return 是否创建成功
     * @throws IOException 异常
     */
    boolean create(IndexDTO indexDTO) throws IOException;

    /**
     * 删除索引
     * @param indexName 索引名
     * @return 是否删除成功
     * @throws IOException 异常
     */
    boolean delete(String indexName) throws IOException;

    /**
     * 索引列表
     * @param queryStr 搜索字符串
     * @param indices 默认显示的索引名
     * @return PageResult
     * @throws IOException 异常
     */
    PageResult<Map<String,String>> list(String queryStr, String indices) throws IOException;

    /**
     * 显示索引明细
     * @param indexName 索引名
     * @return 索引明细
     * @throws IOException 异常
     */
    Map<String,Object> show(String indexName) throws IOException;
}
