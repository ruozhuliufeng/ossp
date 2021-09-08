package cn.aixuxi.ossp.common.log.service;

import cn.aixuxi.ossp.common.log.model.Audit;

/**
 * 审计日志接口
 * @author ruozhuliufeng
 * @date 2021-07-17
 */
public interface IAuditService {
    /**
     * 保存日志信息
     * @param audit 保存审计日志信息
     */
    void save(Audit audit);
}
