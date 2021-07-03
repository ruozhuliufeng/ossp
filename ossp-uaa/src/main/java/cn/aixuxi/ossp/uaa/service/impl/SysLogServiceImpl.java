package cn.aixuxi.ossp.uaa.service.impl;

import cn.aixuxi.ossp.uaa.entity.SysLog;
import cn.aixuxi.ossp.uaa.mapper.SysLogMapper;
import cn.aixuxi.ossp.uaa.service.SysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {
}
