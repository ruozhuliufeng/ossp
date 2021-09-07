package cn.aixuxi.ossp.uaa.controller;

import cn.aixuxi.ossp.common.constant.SecurityConstants;
import cn.aixuxi.ossp.common.model.Result;
import cn.aixuxi.ossp.uaa.service.IValidateCodeService;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 验证码提供
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 13:20
 **/
@Controller
public class ValidateCodeController {
    @Autowired
    private IValidateCodeService validateCodeService;

    /**
     * 创建验证码
     * @param deviceId 机器码
     * @param response response
     * @throws Exception 异常
     */
    @GetMapping(SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/{deviceId}")
    public void createCode(@PathVariable("deviceId") String deviceId, HttpServletResponse response) throws Exception{
        Assert.notNull(deviceId,"机器码不能为空");
        // 设置请求头，输出为图片类型
        CaptchaUtil.setHeader(response);
        // 三个参数分别为宽、搞、位数
        GifCaptcha gifCaptcha = new GifCaptcha(100,35,4);
        // 设置类型：字母数字混合
        gifCaptcha.setCharType(Captcha.TYPE_DEFAULT);
        // 保存验证码
        validateCodeService.saveImageCode(deviceId,gifCaptcha.text().toLowerCase());
        // 输出图片流
        gifCaptcha.out(response.getOutputStream());
    }

    /**
     * 发送手机短信验证码，后续要加接口限制
     * @param mobile 手机号
     * @return R
     */
    @ResponseBody
    @GetMapping(SecurityConstants.MOBILE_VALIDATE_CODE_URL_PREFIX+"/{mobile}")
    public Result createCode(@PathVariable("mobile") String mobile){
        Assert.notNull(mobile,"手机号不能为空！");
        return validateCodeService.sendSmsCode(mobile);
    }
}
