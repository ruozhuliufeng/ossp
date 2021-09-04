package cn.aixuxi.ossp.search.server.search.service.impl;

import cn.aixuxi.ossp.common.es.utils.SearchBuilder;
import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.search.client.model.SearchDTO;
import cn.aixuxi.ossp.search.server.search.service.ISearchService;
import com.fasterxml.jackson.databind.JsonNode;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 通用搜索
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 14:43
 **/
@Service
public class SearchServiceImpl implements ISearchService {

    private final RestHighLevelClient client;

    public SearchServiceImpl(RestHighLevelClient client){
        this.client = client;
    }

    /**
     * StringQuery通用搜索
     *
     * @param indexName 索引名
     * @param searchDTO 搜索DTO
     * @return PageResult
     * @throws IOException 异常
     */
    @Override
    public PageResult<JsonNode> strQuery(String indexName, SearchDTO searchDTO) throws IOException {
        return SearchBuilder.builder(client,indexName)
                .setStringQuery(searchDTO.getQueryStr())
                .addSort(searchDTO.getSortCol(), SortOrder.DESC)
                .setIsHighlight(searchDTO.getIsHighlighter())
                .getPage(searchDTO.getPage(),searchDTO.getLimit());
    }
}
