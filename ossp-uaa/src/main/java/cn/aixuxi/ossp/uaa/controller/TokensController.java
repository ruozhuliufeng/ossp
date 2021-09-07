package cn.aixuxi.ossp.uaa.controller;

import cn.aixuxi.ossp.auth.client.util.AuthUtils;
import cn.aixuxi.ossp.common.constant.SecurityConstants;
import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.common.model.Result;
import cn.aixuxi.ossp.uaa.model.TokenVO;
import cn.aixuxi.ossp.uaa.service.ITokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Toekn管理接口
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 13:01
 **/
@Api(tags = "Token管理")
@Slf4j
@RestController
@RequestMapping("/tokens")
public class TokensController {
    @Resource
    private ITokenService tokenService;
    @Resource
    private ClientDetailsService clientDetailsService;
    @Resource
    private PasswordEncoder passwordEncoder;

    @GetMapping("/list")
    @ApiOperation(value = "token列表")
    public PageResult<TokenVO> list(@RequestParam Map<String,Object> params, String tenantId){
        return tokenService.listTokens(params,tenantId);
    }

    @GetMapping("/key")
    @ApiOperation(value = "获取jwt密钥")
    public Result<String> key(HttpServletRequest request){
        try {
            String[] clientArr = AuthUtils.extractClient(request);
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientArr[0]);
            if (clientDetails == null || !passwordEncoder.matches(clientArr[1],clientDetails.getClientSecret())){
                throw new BadCredentialsException("应用ID或密码错误！");
            }
        }catch (AuthenticationException e){
            return Result.failed(e.getMessage());
        }
        org.springframework.core.io.Resource res = new ClassPathResource(SecurityConstants.RSA_PUBLIC_KEY);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(res.getInputStream()))) {
            return Result.succeed(br.lines().collect(Collectors.joining("\n")));
        }catch (IOException ioe){
            log.error("Key ERROR!",ioe);
            return Result.failed(ioe.getMessage());
        }
    }

}
