package cn.aixuxi.ossp.business.user.mapper;

import cn.aixuxi.ossp.common.db.mapper.SuperMapper;
import cn.aixuxi.ossp.common.model.SysUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:15
 **/
@Mapper
public interface SysUserMapper extends SuperMapper<SysUser> {
    /**
     * 分页查询用户列表
     * @param page 分页信息
     * @param params 参数
     * @return 用户列表
     */
    List<SysUser> findList(Page<SysUser> page,@Param("u") Map<String, Object> params);
}
