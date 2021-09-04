package cn.aixuxi.ossp.search.server.admin.controller;

import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.common.model.Result;
import cn.aixuxi.ossp.search.server.admin.model.IndexDTO;
import cn.aixuxi.ossp.search.server.admin.properties.IndexProperties;
import cn.aixuxi.ossp.search.server.admin.service.IIndexService;
import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * 索引管理
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 14:04
 **/
@Slf4j
@RestController
@Api(tags = "索引管理api")
@RequestMapping("/admin")
public class IndexController {

    @Autowired
    private IIndexService indexService;

    @Autowired
    private IndexProperties properties;

    /**
     * 创建索引
     * @param indexDTO 索引数据
     * @return Result
     */
    @PostMapping("/index")
    public Result createIndex(@RequestBody IndexDTO indexDTO) throws IOException{
        if (indexDTO.getNumberOfShards() == null){
            indexDTO.setNumberOfShards(1);
        }
        if (indexDTO.getNumberOfReplicas() == null){
            indexDTO.setNumberOfReplicas(0);
        }
        indexService.create(indexDTO);
        return Result.succeed("操作成功");
    }

    /**
     * 索引列表
     * @param queryStr 搜索字符串
     * @return PageResult
     */
    @GetMapping("/indices")
    public PageResult<Map<String,String>> list(@RequestParam(required = true) String queryStr) throws IOException{
        return indexService.list(queryStr,properties.getShow());
    }

    /**
     * 索引明细
     * @param indexName 索引名称
     * @return Result
     */
    @GetMapping("/index")
    public Result<Map<String,Object>> showIndex(String indexName) throws IOException{
        Map<String,Object> result = indexService.show(indexName);
        return Result.succeed(result);
    }

    /**
     * 删除索引
     * @param indexName 索引名称
     * @return Result
     */
    @DeleteMapping("/index")
    public Result deleteIndex(String indexName) throws IOException{
        indexService.delete(indexName);
        return Result.succeed("操作成功");
    }
}
