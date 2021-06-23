package cn.aixuxi.ossp.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 类描述 请求工具类
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021/5/28 22:35
 */
public class RequestParamsUtils {
    /**
     * 功能描述: Get请求地址拼接
     * @param reqUrl 请求地址
     * @param params 参数
     * @return : java.lang.String
     * @author : ruozhuliufeng
     * @date : 2021/5/26 22:25
     */
    public static String getStringData(String reqUrl, Map<String,String> params){
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = String.valueOf(keys.get(i));
            Object value =  params.get(key);
            if (value != null) {
                content.append((i == 0 ? "" : "&") + key + "=" + value);
            } else {
                content.append((i == 0 ? "" : "&") + key + "=");
            }
        }
        return reqUrl+"?"+ content;
    }
}
