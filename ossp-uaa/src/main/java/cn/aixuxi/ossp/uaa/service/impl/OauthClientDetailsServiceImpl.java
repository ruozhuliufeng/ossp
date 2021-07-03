package cn.aixuxi.ossp.uaa.service.impl;

import cn.aixuxi.ossp.uaa.entity.OauthClientDetails;
import cn.aixuxi.ossp.uaa.mapper.OauthClientDetailsMapper;
import cn.aixuxi.ossp.uaa.service.OauthClientDetailsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 类描述 客户端详情服务
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021/5/30 20:46
 */
@Service
public class OauthClientDetailsServiceImpl
        extends ServiceImpl<OauthClientDetailsMapper, OauthClientDetails>
        implements OauthClientDetailsService {

    @Override
    public OauthClientDetails getByClientId(String clientId) {
        return this.getBaseMapper().selectOne(new QueryWrapper<OauthClientDetails>().eq("client_id",clientId));
    }
}
