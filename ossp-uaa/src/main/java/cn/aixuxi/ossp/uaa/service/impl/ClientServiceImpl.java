package cn.aixuxi.ossp.uaa.service.impl;

import cn.aixuxi.ossp.common.constant.SecurityConstants;
import cn.aixuxi.ossp.common.lock.DistributedLock;
import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.common.model.Result;
import cn.aixuxi.ossp.common.service.impl.SuperServiceImpl;
import cn.aixuxi.ossp.redis.template.RedisRepository;
import cn.aixuxi.ossp.uaa.mapper.ClientMapper;
import cn.aixuxi.ossp.uaa.model.Client;
import cn.aixuxi.ossp.uaa.service.IClientService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jodd.cli.Cli;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 13:19
 **/
public class ClientServiceImpl extends SuperServiceImpl<ClientMapper, Client>
        implements IClientService {
    private final static String LOCK_KEY_CLIENTID = "clientId:";

    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DistributedLock lock;
    /**
     * 添加应用(申请接入)
     *
     * @param client 应用信息
     * @return 统一返回格式
     */
    @Override
    public Result saveClient(Client client) throws Exception {
        client.setClientSecret(passwordEncoder.encode(client.getClientSecretStr()));
        String clientId = client.getClientId();
        // 操作幂等性
        super.saveOrUpdateIdempotency(client,lock,
                LOCK_KEY_CLIENTID+clientId,
                new QueryWrapper<Client>().eq("client_id",clientId),
                clientId +"已存在");
        return Result.succeed("操作成功");
    }

    /**
     * 查询应用列表
     *
     * @param params 参数
     * @param isPage 是否分页
     * @return PageResult
     */
    @Override
    public PageResult<Client> listClient(Map<String, Object> params, boolean isPage) {
        Page<Client> page;
        if (isPage){
            page = new Page<>(MapUtils.getInteger(params,"page"),MapUtils.getInteger(params,"limit"));
        }else{
            page = new Page<>(1,1);
        }
        List<Client> list = baseMapper.findList(page,params);
        page.setRecords(list);
        return PageResult.<Client>builder().data(list).code(0).count(page.getTotal()).build();
    }

    /**
     * 删除应用
     *
     * @param id 应用id
     */
    @Override
    public void delClient(long id) {
        String clientId = baseMapper.selectById(id).getClientId();
        baseMapper.deleteById(id);
        redisRepository.del(clientRedisKey(clientId));
    }

    private String clientRedisKey(String clientId) {
        return SecurityConstants.CACHE_CLIENT_KEY + ":" +clientId;
    }

    /**
     * 获取应用数据
     *
     * @param clientId 应用i的
     * @return 应用数据
     */
    @Override
    public Client loadClientByClientId(String clientId) {
        QueryWrapper<Client> wrapper = Wrappers.query();
        wrapper.eq("client_id",clientId);
        return this.getOne(wrapper);
    }
}
