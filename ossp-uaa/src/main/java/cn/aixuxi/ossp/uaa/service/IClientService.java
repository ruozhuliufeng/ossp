package cn.aixuxi.ossp.uaa.service;

import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.common.model.Result;
import cn.aixuxi.ossp.common.service.ISuperService;
import cn.aixuxi.ossp.uaa.model.Client;
import jodd.cli.Cli;

import java.util.Map;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 13:09
 **/
public interface IClientService extends ISuperService<Client> {
    /**
     * 添加应用(申请接入)
     * @param clientDTO 应用信息
     * @return 统一返回格式
     */
    Result saveClient(Client clientDTO) throws Exception;

    /**
     * 查询应用列表
     * @param params 参数
     * @param isPage 是否分页
     * @return PageResult
     */
    PageResult<Client> listClient(Map<String,Object> params,boolean isPage);

    /**
     * 删除应用
     * @param id 应用id
     */
    void delClient(long id);

    /**
     * 获取应用数据
     * @param clientId 应用i的
     * @return 应用数据
     */
    Client loadClientByClientId(String clientId);
}
