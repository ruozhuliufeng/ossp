package cn.aixuxi.ossp.common.db.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * Mapper父类，注意这个类不要让MP扫描到
 * @author ruozhuliufeng
 */
public interface SuperMapper<T> extends BaseMapper<T> {
    // 这里可以放一些公共的方法
}
