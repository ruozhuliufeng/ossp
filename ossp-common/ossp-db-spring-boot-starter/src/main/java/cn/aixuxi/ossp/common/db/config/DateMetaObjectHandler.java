package cn.aixuxi.ossp.common.db.config;

import cn.aixuxi.ossp.common.db.properties.MybatisPlusAutoFillProperties;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 自定义填充公共字段
 * @author ruozhuliufeng
 * @date 2021-08-21
 */
public class DateMetaObjectHandler implements MetaObjectHandler {

    private MybatisPlusAutoFillProperties autoFillProperties;

    public DateMetaObjectHandler(MybatisPlusAutoFillProperties autoFillProperties){
        this.autoFillProperties = autoFillProperties;
    }

    /**
     * 是否开启了插入填充
     * @return 是否
     */
    @Override
    public boolean openInsertFill() {
        return autoFillProperties.getEnableInsertFill();
    }

    /**
     * 是否开启了更新填充
     * @return 是否
     */
    @Override
    public boolean openUpdateFill() {
        return autoFillProperties.getEnableUpdateFill();
    }

    /**
     * 插入填充，字段为空自动填充
     * @param metaObject object
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = getFieldValByName(autoFillProperties.getCreateTimeField(), metaObject);
        Object updateTime = getFieldValByName(autoFillProperties.getUpdateTimeField(), metaObject);
        if (createTime == null || updateTime == null){
            Date date = new Date();
            if (createTime == null){
                setFieldValByName(autoFillProperties.getCreateTimeField(),date, metaObject);
            }
            if (updateTime == null){
                setFieldValByName(autoFillProperties.getUpdateTimeField(), date,metaObject);
            }
        }
    }

    /**
     * 更新字段填充
     * @param metaObject object
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName(autoFillProperties.getUpdateTimeField(),new Date(),metaObject);
    }
}
