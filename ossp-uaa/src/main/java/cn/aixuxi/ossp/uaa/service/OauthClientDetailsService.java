package cn.aixuxi.ossp.uaa.service;

import cn.aixuxi.ossp.uaa.entity.OauthClientDetails;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OauthClientDetailsService extends IService<OauthClientDetails> {
    // 根据clientId获取客户端详情服务
    OauthClientDetails getByClientId(String clientId);
}
