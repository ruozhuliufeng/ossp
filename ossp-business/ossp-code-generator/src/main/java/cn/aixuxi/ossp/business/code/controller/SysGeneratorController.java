package cn.aixuxi.ossp.business.code.controller;

import cn.aixuxi.ossp.business.code.service.SysGeneratorService;
import cn.aixuxi.ossp.common.model.PageResult;
import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 代码生成器
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-09 13:56
 **/
@RestController
@Api(tags = "代码生成器")
@RequestMapping("/generator")
public class SysGeneratorController {
    @Autowired
    private SysGeneratorService sysGeneratorService;

    /**
     * 获取表列表
     * @param params 参数
     * @return 列表
     */
    @GetMapping("/list")
    public PageResult getTableList(@RequestParam Map<String,Object> params){
        return sysGeneratorService.queryList(params);
    }

    /**
     * 生成代码FileUtil
     * @param tables 表名
     * @param response 返回
     * @throws IOException 生成异常
     */
    public void makeCode(String tables, HttpServletResponse response) throws IOException{
        byte[] data = sysGeneratorService.generatorCode(tables.split(","));
        response.reset();
        response.setHeader("Content-Disposition","attachment;filename=\"generator.zip\"");
        response.addHeader("Content-Length",""+data.length);
        response.setContentType("application/octet-stream;charset=UTF-8");
        IOUtils.write(data,response.getOutputStream());
    }
}
