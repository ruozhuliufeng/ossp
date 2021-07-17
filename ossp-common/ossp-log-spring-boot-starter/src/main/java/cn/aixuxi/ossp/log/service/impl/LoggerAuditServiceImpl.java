package cn.aixuxi.ossp.log.service.impl;

import cn.aixuxi.ossp.log.model.Audit;
import cn.aixuxi.ossp.log.service.IAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.time.format.DateTimeFormatter;

/**
 * 审计日志实现类-打印日志
 * @author ruozhuliufeng
 * @date 2021-07-17
 */
@Slf4j
@ConditionalOnProperty(name = "ossp-audit-log.log-type",havingValue = "logger",matchIfMissing = true)
public class LoggerAuditServiceImpl implements IAuditService {

    private static final String MSG_PATTERN = "{}|{}|{}|{}|{}|{}|{}|{}";

    /**
     *
     * 打印日志信息，格式为{时间}|{应用名}|{类名}|{方法名}|{用户id}|{用户名}|{租户id}|{操作信息}
     * @param audit 打印审计日志信息
     */
    @Override
    public void save(Audit audit) {
        log.debug(MSG_PATTERN,
                audit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                audit.getApplicationName(),audit.getClassName(),audit.getMethodName(),
                audit.getUserId(),audit.getUserName(),
                audit.getClientId(),audit.getOperation());
    }
}
