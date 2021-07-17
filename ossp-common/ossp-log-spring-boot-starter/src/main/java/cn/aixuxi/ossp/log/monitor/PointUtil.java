package cn.aixuxi.ossp.log.monitor;

import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

/**
 * 日志埋点工具类
 * @author ruozhuliufeng
 * @date 2021-07-17
 */
@Slf4j
public class PointUtil {
    private static final String MSG_PATTREN = "{}|{}|{}";
    private static final String PROPERTIES_SPLIT = "&";
    private static final String PROPERTIES_VALE_SPLIT = "=";
    private final PointEntry pointEntry;
    private PointUtil(){ pointEntry = new PointEntry();}

    @Getter
    @Setter
    private class PointEntry{
        String id;
        String type;
        Object properties;
    }

    /**
     * 格式为：{时间}|{来源}|{对象id}|{类型}|{对象属性(（)以&分割)}
     * 例子:2021-07-17 22:22:22|ossp-uaa|1|user-login|ip=xxx.xxx.xx&userName=张三&userType=后台管理员
     * @param id 对象id
     * @param type 类型
     * @param message 对象属性
     */
    public static void info(String id,String type,String message){
        log.info(MSG_PATTREN,id,type,message);
    }

    public static void debug(String id,String type,String message){
        log.debug(MSG_PATTREN,id,type,message);
    }

    public static PointUtil builder(){
        return new PointUtil();
    }

    /**
     * @param businessId 业务id/对象id
     */
    public PointUtil id(Object businessId){
        this.pointEntry.setId(String.valueOf(businessId));
        return this;
    }

    /**
     * @param type 类型
     */
    public PointUtil type(String type){
        this.pointEntry.setType(type);
        return this;
    }
    /**
     * @param properties 对象属性
     */
    public PointUtil properties(Object properties){
        this.pointEntry.setProperties(properties);
        return this;
    }

    private String getPropertiesStr() {
        Object properties = this.pointEntry.getProperties();
        StringBuilder result = new StringBuilder();
        if (!ObjectUtils.isEmpty(properties)){
            // 解析Map
            if (properties instanceof Map){
                Map proMap = (Map) properties;
                Iterator<Map.Entry> ite = proMap.entrySet().iterator();
                while (ite.hasNext()){
                    Map.Entry entry = ite.next();
                    if (result.length()>0){
                        result.append(PROPERTIES_SPLIT);
                    }
                    result.append(entry.getKey()).append(PROPERTIES_VALE_SPLIT).append(entry.getKey());
                }
            }else {// 解析对象
                Field[] allFields = ReflectUtil.getFields(properties.getClass());
                for (Field field : allFields){
                    String fieldName = field.getName();
                    Object fieldValue = ReflectUtil.getFieldValue(properties,field);
                    if (result.length()>0){
                        result.append(PROPERTIES_SPLIT);
                    }
                    result.append(fieldName).append(PROPERTIES_VALE_SPLIT).append(fieldValue);
                }
            }
        }
        return result.toString();
    }

    public void build(){
        PointUtil.debug(this.pointEntry.getId(),this.pointEntry.getType(),
        this.getPropertiesStr());
    }


}
