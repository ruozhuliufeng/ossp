package cn.aixuxi.ossp.uaa.mapper;

import cn.aixuxi.ossp.common.db.mapper.SuperMapper;
import cn.aixuxi.ossp.uaa.model.Client;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 客户端
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 11:24
 **/
@Mapper
public interface ClientMapper extends SuperMapper<Client> {
    List<Client> findList(Page<Client> page,@Param("params") Map<String,Object> params);
}
