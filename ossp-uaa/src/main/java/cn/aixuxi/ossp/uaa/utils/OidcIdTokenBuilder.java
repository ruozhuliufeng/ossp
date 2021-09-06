package cn.aixuxi.ossp.uaa.utils;

import cn.aixuxi.ossp.auth.client.util.JwtUtils;
import cn.aixuxi.ossp.common.utils.JsonUtil;
import lombok.Getter;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.util.Assert;
import java.util.LinkedHashMap;
import java.util.Map;

import static cn.aixuxi.ossp.auth.client.constants.IdTokenClaimNames.*;

/**
 * Oidc协议的IdToken
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 11:29
 **/
@Getter
public class OidcIdTokenBuilder {
    private final Map<String,Object> claims;
    private final KeyProperties keyProperties;

    private OidcIdTokenBuilder(KeyProperties keyProperties){
        this.claims = new LinkedHashMap<>();
        this.keyProperties = keyProperties;
    }

    public static OidcIdTokenBuilder builder(KeyProperties keyProperties){
        Assert.notNull(keyProperties,"keyProperties是必须的");
        return new OidcIdTokenBuilder(keyProperties);
    }

    /**
     * 在结果中使用此声明 {@link OidcIdTokenBuilder}
     * @param name 名称
     * @param value 值
     * @return OidcIdTokenBuilder
     */
    public OidcIdTokenBuilder claim(String name,Object value){
        this.claims.put(name,value);
        return this;
    }

    /**
     * 在结果中使用 {@link OidcIdTokenBuilder}
     * @param audience audience
     * @return OidcIdTokenBuilder
     */
    public OidcIdTokenBuilder audience(String audience){
        return claim(AUD,audience);
    }

    /**
     * 添加过期时间 {@link OidcIdTokenBuilder}
     * @param expiresAt 过期时长
     * @return OidcIdTokenBuilder
     */
    public OidcIdTokenBuilder expiresAt(long expiresAt){
        return this.claim(EXP,expiresAt);
    }


    /**
     * 赋值issue {@link OidcIdTokenBuilder}
     * @param issueAt issueAt
     * @return OidcIdTokenBuilder
     */
    public OidcIdTokenBuilder issueAt(long issueAt){
        return this.claim(IAT,issueAt);
    }

    /**
     * 赋值 {@link OidcIdTokenBuilder}
     * @param nonce nonce
     * @return OidcIdTokenBuilder
     */
    public OidcIdTokenBuilder nonce(String nonce){
        return this.claim(NONCE,nonce);
    }

    /**
     * 赋值主体 {@link OidcIdTokenBuilder}
     * @param subject 主体
     * @return OidcIdTokenBuilder
     */
    public OidcIdTokenBuilder subject(String subject){
        return this.claim(SUB,subject);
    }

    /**
     * 赋值用户姓名 {@link OidcIdTokenBuilder}
     * @param name 用户姓名
     * @return OidcIdTokenBuilder
     */
    public OidcIdTokenBuilder name(String name){
        return this.claim(NAME,name);
    }

    /**
     * 赋值用户登录名 {@link OidcIdTokenBuilder}
     * @param loginName 登录名
     * @return OidcIdTokenBuilder
     */
    public OidcIdTokenBuilder loginName(String loginName){
        return this.claim(L_NAME,loginName);
    }

    /**
     * 赋值用户头像 {@link OidcIdTokenBuilder}
     * @param picture 头像地址
     * @return OidcIdTokenBuilder
     */
    public OidcIdTokenBuilder picture(String picture){
        return this.claim(PIC,picture);
    }

    /**
     * bulid  {@link OidcIdTokenBuilder}
     * @return String
     */
    public String build(){
        return JwtUtils.encodeStr(JsonUtil.toJSONString(claims),keyProperties);
    }
}
