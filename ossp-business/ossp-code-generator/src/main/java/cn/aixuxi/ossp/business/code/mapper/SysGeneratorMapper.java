package cn.aixuxi.ossp.business.code.mapper;

import cn.aixuxi.ossp.common.db.mapper.SuperMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 代码生成
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-09 13:37
 **/
@Mapper
@Component
public interface SysGeneratorMapper extends SuperMapper {
    List<Map<String,Object>> queryList(Page<Map<String,Object>> page,@Param("r") Map<String,Object> map);

    int queryTotal(Map<String,Object> map);

    Map<String,String> queryTable(String tableName);

    List<Map<String,String>> queryColumns(String tableName);
}
