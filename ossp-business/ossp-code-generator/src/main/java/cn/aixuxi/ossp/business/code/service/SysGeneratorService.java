package cn.aixuxi.ossp.business.code.service;

import cn.aixuxi.ossp.common.model.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 代码生成服务
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-09 13:47
 **/
public interface SysGeneratorService {
    PageResult queryList(Map<String,Object> map);

    Map<String,String> queryTable(String tableName);

    List<Map<String,String>> queryColumns(String tableName);

    byte[] generatorCode(String[] tableNames);
}
