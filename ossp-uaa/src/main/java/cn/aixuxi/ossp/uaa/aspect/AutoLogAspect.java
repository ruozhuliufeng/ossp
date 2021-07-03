package cn.aixuxi.ossp.uaa.aspect;

import cn.aixuxi.ossp.common.constant.SysConst;
import cn.aixuxi.ossp.common.utils.IPUtils;
import cn.aixuxi.ossp.common.utils.SpringContextUtils;
import cn.aixuxi.ossp.uaa.annotation.AutoLog;
import cn.aixuxi.ossp.uaa.entity.SysLog;
import cn.aixuxi.ossp.uaa.service.SysLogService;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 系统日志，切面处理
 * @author ruozhuliufeng
 */
@Aspect
@Component
public class AutoLogAspect {
    @Autowired
    private SysLogService sysLogService;

    /**
     * 切点
     */
    @Pointcut("@annotation(cn.aixuxi.auth.server.annotation.AutoLog)")
    public void logPointCut(){}

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable{
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = point.proceed();
        // 执行时长
        long time = System.currentTimeMillis() - beginTime;
        // 保存日志
        saveSysLog(point,time);

        return result;
    }

    /**
     * 保存日志
     * @param joinPoint 切点
     * @param time 时长
     */
    private void saveSysLog(ProceedingJoinPoint joinPoint,long time){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog log = new SysLog();
        AutoLog autoLog = method.getAnnotation(AutoLog.class);
        if (autoLog!=null){
            // 注解上的描述，操作日志内容
            log.setLogContent(autoLog.value());
            log.setLogType(autoLog.logType());
        }
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.setMethod(className + '.' + methodName + "()");

        // 设置操作类型
        if (log.getLogType() == SysConst.LOG_TYPE_2){
            log.setOperateType(getOperateType(methodName,autoLog.operateType()));
        }
        // 请求的参数
        Object[] args = joinPoint.getArgs();
        try{
            String params = JSONObject.toJSONString(args);
            log.setRequestParam(params);
        }catch (Exception e){

        }
        // 获取request
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        // 设置IP地址
        log.setIp(IPUtils.getIpAddr(request));
        // 获取登录用户信息
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        String userJson = (String) JSONObject.parseObject(json).get("username");
//        SysUser user = JSONObject.parseObject(userJson,SysUser.class);
        if (username!=null){
//            log.setUserid(user.getId().toString());
            log.setUsername(username);
        }
        // 耗时
        log.setCostTime(time);
        // 保存系统日志
        sysLogService.save(log);
    }

    /**
     * 获取操作类型
     * @param methodName 方法名
     * @param operateType 操作类型
     * @return 操作类型
     */
    private int getOperateType(String methodName,int operateType){
        if (operateType>0){
            return operateType;
        }
        if (methodName.startsWith("list")){
            return SysConst.OPERATE_TYPE_1;
        }
        if (methodName.startsWith("save")){
            return SysConst.OPERATE_TYPE_2;
        }
        if (methodName.startsWith("update")){
            return SysConst.OPERATE_TYPE_3;
        }
        if (methodName.startsWith("delete")){
            return SysConst.OPERATE_TYPE_4;
        }
        if (methodName.startsWith("import")){
            return SysConst.OPERATE_TYPE_5;
        }
        if (methodName.startsWith("export")){
            return SysConst.OPERATE_TYPE_6;
        }
        return SysConst.OPERATE_TYPE_1;
    }
}
