package cn.aixuxi.ossp.monitor.log.center.controller;

import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.search.client.client.service.IQueryService;
import cn.aixuxi.ossp.search.client.model.SearchDTO;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统日志
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-09 14:20
 **/
@RestController
public class SysLogController {
    @Autowired
    private IQueryService queryService;

    @ApiOperation(value = "系统日志全文搜索列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "分页起始位置",required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "limit",value = "分页结束位置",required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "queryStr",value = "搜索关键字",dataType = "String")
    })
    @GetMapping(value = "/sysLog")
    public PageResult<JsonNode> getPage(SearchDTO searchDTO){
        searchDTO.setIsHighlighter(true);
        searchDTO.setSortCol("timestamp");
        return queryService.strQuery("sys-log-*",searchDTO);
    }
}
