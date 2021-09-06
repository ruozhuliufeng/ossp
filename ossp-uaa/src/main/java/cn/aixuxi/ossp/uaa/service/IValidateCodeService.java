package cn.aixuxi.ossp.uaa.service;

import cn.aixuxi.ossp.common.model.Result;

/**
 * 验证码服务
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 13:15
 **/
public interface IValidateCodeService {

    /**
     * 保存图形验证码
     * @param deviceId 前端唯一标识
     * @param imageCode 验证码
     */
    void saveImageCode(String deviceId,String imageCode);

    /**
     * 发送短信验证码
     * @param mobile 手机号
     * @return Result
     */
    Result sendSmsCode(String mobile);

    /**
     * 获取验证码
     * @param deviceId 前端唯一标识/手机号
     * @return 验证码
     */
    String getCode(String deviceId);

    /**
     * 删除验证码
     * @param deviceId 前端唯一标识/手机号
     */
    void remove(String deviceId);

    /**
     * 验证验证码
     * @param deviceId 前端唯一标识/手机号
     * @param validCode 验证码
     */
    void validate(String deviceId,String validCode);
}
