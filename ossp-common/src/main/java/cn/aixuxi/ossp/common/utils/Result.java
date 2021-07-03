package cn.aixuxi.ossp.common.utils;

import cn.aixuxi.ossp.common.constant.SysConst;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用返回工具类
 * @author ruozhulifeng
 */
@Data
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success = true;

    /**
     * 返回代码
     */
    private Integer code = 0;
    /**
     * 返回处理消息
     */
    private String message = "操作成功";
    /**
     * 返回数据对象
     */
    private Object data;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    private Map<String,Object> map;

    private List<Object> list;

    public Result setMap(Map<String,Object> map){
        this.map = map;
        return this;
    }

    /**
     * Map对象添加值
     * @param key 键
     * @param value 值
     * @return 处理结果
     */
    public Result addAttribute(String key,Object value){
        if (this.map!=null){
            this.map.put(key,value);
        }else {
            this.map = new HashMap<String,Object>();
            this.map.put(key,value);
        }
        return this;
    }

    public Result(){
        this.code = SysConst.SC_OK_200;
        this.success = true;
    }

    public Result success(String message){
        this.message = message;
        this.code = SysConst.SC_OK_200;
        this.success = true;
        return this;
    }

    public static Result ok(){
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(SysConst.SC_OK_200);
        result.setMessage("操作成功");
        return result;
    }

    public static Result ok(String msg){
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(SysConst.SC_OK_200);
        result.setMessage(msg);
        return result;
    }

    public static Result ok(Object data){
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(SysConst.SC_OK_200);
        result.setData(data);
        return result;
    }

    public static Result error(int code,String msg){
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }

    public static Result error(String msg){
        return error(SysConst.SC_INTERNAL_SERVER_ERROR_500,msg);
    }

    public Result error500(String msg){
        this.message = msg;
        this.code = SysConst.SC_INTERNAL_SERVER_ERROR_500;
        this.success = false;
        return this;
    }

    public static Result noauth(String msg){
        return error(SysConst.SC_JEECG_NO_AUTHZ,msg);
    }
}
