package cn.aixuxi.ossp.uaa.service.impl;

import cn.aixuxi.ossp.common.constant.SecurityConstants;
import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.redis.template.RedisRepository;
import cn.aixuxi.ossp.uaa.model.TokenVO;
import cn.aixuxi.ossp.uaa.service.ITokenService;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Token管理服务(Redis Token)
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 9:24
 **/
@Slf4j
@Service
public class RedisTokenServiceImpl implements ITokenService {
    @Autowired
    private RedisRepository redisRepository;

    /**
     * 查询token列表
     *
     * @param params   请求参数
     * @param clientId 应用id
     * @return PageResult
     */
    @Override
    public PageResult<TokenVO> listTokens(Map<String, Object> params, String clientId) {
        Integer page = MapUtils.getInteger(params, "page");
        Integer limit = MapUtils.getInteger(params, "limit");
        int[] startEnds = PageUtil.transToStartEnd(page, limit);
        // 根据请求参数生成redis的key
        String redisKey = getRedisKey(params,clientId);
        long size = redisRepository.length(redisKey);
        List<TokenVO> result = new ArrayList<>(limit);
        RedisSerializer<Object> valueSerializer = RedisSerializer.java();
        // 查询token集合
        List<Object> tokenObj = redisRepository.getList(redisKey,startEnds[0],startEnds[1]-1,valueSerializer);
        if (tokenObj != null){
            for (Object obj : tokenObj){
                DefaultOAuth2AccessToken accessToken = (DefaultOAuth2AccessToken) obj;
                // 构造token独享
                TokenVO tokenVO = new TokenVO();
                tokenVO.setTokenValue(accessToken.getValue());
                tokenVO.setExpiration(accessToken.getExpiration());
                // 获取用户信息
                Object authObj = redisRepository.get(SecurityConstants.REDIS_TOKEN_AUTH + accessToken.getValue(),valueSerializer);
                OAuth2Authentication authentication = (OAuth2Authentication) authObj;
                if (authentication != null){
                    OAuth2Request request = authentication.getOAuth2Request();
                    tokenVO.setUsername(authentication.getName());
                    tokenVO.setClientId(request.getClientId());
                    tokenVO.setGrantType(request.getGrantType());
                }
                Map<String,Object> additionalInformation = accessToken.getAdditionalInformation();
                String accountType = (String) additionalInformation.get(SecurityConstants.ACCOUNT_TYPE_PARAM_NAME);
                tokenVO.setAccountType(accountType);
                result.add(tokenVO);
            }
        }
        return PageResult.<TokenVO>builder().data(result).code(0).count(size).build();
    }

    /**
     * 根据请求参数生成redis的key
     * @param params 请求参数
     * @param clientId 请求参数
     * @return redis的key
     */
    private String getRedisKey(Map<String, Object> params, String clientId) {
        String result;
        String username = MapUtils.getString(params,"username");
        if (StrUtil.isNotEmpty(username)){
            result = SecurityConstants.REDIS_UNAME_TO_ACCESS + clientId +":"+username;
        }else {
            result = SecurityConstants.REDIS_CLIENT_ID_TO_ACCESS + clientId;
        }
        return result;
    }
}
