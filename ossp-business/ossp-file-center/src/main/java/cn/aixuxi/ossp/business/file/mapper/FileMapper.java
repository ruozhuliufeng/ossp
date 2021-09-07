package cn.aixuxi.ossp.business.file.mapper;

import cn.aixuxi.ossp.business.file.model.FileInfo;
import cn.aixuxi.ossp.common.db.mapper.SuperMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 上传存储 db
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 17:10
 **/
@Mapper
public interface FileMapper extends SuperMapper<FileInfo> {
    List<FileInfo> findList(Page<FileInfo> page,@Param("f") Map<String,Object> params);
}
