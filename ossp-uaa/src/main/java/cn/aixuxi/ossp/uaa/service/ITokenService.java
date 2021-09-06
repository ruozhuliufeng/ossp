package cn.aixuxi.ossp.uaa.service;

import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.uaa.model.TokenVO;

import java.util.Map;

/**
 * Token服务
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 13:12
 **/
public interface ITokenService {

    /**
     * 查询token列表
     * @param params 请求参数
     * @param clientId 应用id
     * @return PageResult
     */
    PageResult<TokenVO> listTokens(Map<String,Object> params,String clientId);
}
