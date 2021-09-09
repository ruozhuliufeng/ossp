package cn.aixuxi.ossp.business.user.mapper;

import cn.aixuxi.ossp.common.db.mapper.SuperMapper;
import cn.aixuxi.ossp.common.model.SysRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 角色
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:14
 **/
@Mapper
public interface SysRoleMapper extends SuperMapper<SysRole> {
    List<SysRole> findAll();

    List<SysRole> findList(Page<SysRole> page,@Param("r") Map<String, Object> params);
}
