package cn.aixuxi.ossp.search.server.search.controller;

import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.search.client.model.SearchDTO;
import cn.aixuxi.ossp.search.server.search.service.ISearchService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 通用搜索
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 15:16
 **/
@Slf4j
@RestController
@Api(tags = "搜索模块api")
@RequestMapping("/search")
public class SearchController {

    private final ISearchService searchService;

    public SearchController(ISearchService searchService){
        this.searchService = searchService;
    }

    /**
     * 查询文档列表
     * @param indexName 索引名
     * @param searchDTO 搜索DTO
     * @return PageResult
     */
    @PostMapping("/{indexName}")
    public PageResult<JsonNode> strQuery(@PathVariable("indexName") String indexName,@RequestBody(required = false) SearchDTO searchDTO) throws IOException{
        if (searchDTO == null){
            searchDTO = new SearchDTO();
        }
        return searchService.strQuery(indexName,searchDTO);
    }
}
