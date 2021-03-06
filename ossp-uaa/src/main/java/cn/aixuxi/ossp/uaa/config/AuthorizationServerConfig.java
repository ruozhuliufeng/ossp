package cn.aixuxi.ossp.uaa.config;

import cn.aixuxi.ossp.auth.client.constants.IdTokenClaimNames;
import cn.aixuxi.ossp.auth.client.properties.TokenStoreProperties;
import cn.aixuxi.ossp.auth.client.util.AuthUtils;
import cn.aixuxi.ossp.common.constant.SecurityConstants;
import cn.aixuxi.ossp.common.model.SysUser;
import cn.aixuxi.ossp.uaa.model.Client;
import cn.aixuxi.ossp.uaa.service.IClientService;
import cn.aixuxi.ossp.uaa.service.impl.RedisClientDetailsService;
import cn.aixuxi.ossp.uaa.service.impl.UserDetailServiceFactory;
import cn.aixuxi.ossp.uaa.utils.OidcIdTokenBuilder;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * OAuth2 ?????????????????????
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 13:47
 **/
@Configuration
@EnableAuthorizationServer
@AutoConfigureAfter(AuthorizationServerEndpointsConfigurer.class)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    /**
     * ??????authenticationManager?????????password grant type
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    @Resource
    private UserDetailServiceFactory userDetailServiceFactory;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private WebResponseExceptionTranslator webResponseExceptionTranslator;
    @Autowired
    private RedisClientDetailsService clientDetailsService;
    @Autowired
    private RandomValueAuthorizationCodeServices authorizationCodeServices;
    @Autowired
    private TokenGranter tokenGranter;

    /**
     * ??????????????????????????????????????????
     * TokenStore,TOkenGranter,OAuth2RequestFactory
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
//                .userDetailsService(userDetailsService)
                .authorizationCodeServices(authorizationCodeServices)
                .exceptionTranslator(webResponseExceptionTranslator)
                .tokenGranter(tokenGranter);
    }

    /**
     * ???????????????????????????id?????????OAuth2????????????????????????
     *
     * @param clients ?????????
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
        clientDetailsService.loadAllClientToCache();
    }

    /**
     * ???????????????AuthorizationServer???????????????????????????<br>
     * ??????ClientCredentialsTokenEndPotingFilter???????????????
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("permitAll()")
                // ???/oauth/token??????client_id??????client_secret???????????????
                .allowFormAuthenticationForClients();
    }

    @Bean
    @Order(1)
    public TokenEnhancer tokenEnhancer(@Autowired(required = false) KeyProperties keyProperties,
                                       IClientService clientService,
                                       TokenStoreProperties tokenStoreProperties) {
        return (accessToken, authentication) -> {
            Set<String> responseTypes = authentication.getOAuth2Request().getResponseTypes();
            Map<String, Object> additionalInfo = new HashMap<>(3);
            String accountType = AuthUtils.getAccountType(authentication.getUserAuthentication());
            if (StrUtil.isNotEmpty(accountType)) {
                additionalInfo.put(SecurityConstants.ACCOUNT_TYPE_PARAM_NAME, accountType);
            }
            if (responseTypes.contains(SecurityConstants.ID_TOKEN)
                    || "authJwt".equals(tokenStoreProperties.getType())) {
                Object principal = authentication.getPrincipal();
                // ??????id??????
                if (principal instanceof SysUser) {
                    SysUser user = (SysUser) principal;
                    if (responseTypes.contains(SecurityConstants.ID_TOKEN)) {
                        // ??????id_token
                        setIdToken(additionalInfo, authentication, keyProperties, clientService, user);
                    }
                    if ("authJwt".equals(tokenStoreProperties.getType())) {
                        additionalInfo.put("id", user.getId());
                    }
                    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
                }
            }
            return accessToken;
        };
    }

    /**
     * ??????id_token
     *
     * @param additionalInfo ??????token??????????????????
     * @param authentication ????????????
     * @param keyProperties  ??????
     * @param clientService  ??????service
     * @param user           ????????????
     */
    private void setIdToken(Map<String, Object> additionalInfo,
                            OAuth2Authentication authentication,
                            KeyProperties keyProperties,
                            IClientService clientService,
                            SysUser user) {
        String clientId = authentication.getOAuth2Request().getClientId();
        Client client = clientService.loadClientByClientId(clientId);
        if (client.getSupportIdToken()) {
            String nonce = authentication.getOAuth2Request().getRequestParameters().get(IdTokenClaimNames.NONCE);
            long now = System.currentTimeMillis();
            long expireAt = System.currentTimeMillis() + client.getIdTokenValiditySeconds() * 1000;
            String idToken = OidcIdTokenBuilder.builder(keyProperties)
                    .issuer(SecurityConstants.ISS)
                    .issueAt(now)
                    .expiresAt(expireAt)
                    .subject(String.valueOf(user.getId()))
                    .name(user.getNickname())
                    .loginName(user.getUsername())
                    .picture(user.getHeadImgUrl())
                    .audience(clientId)
                    .nonce(nonce)
                    .build();
            additionalInfo.put(SecurityConstants.ID_TOKEN, idToken);
        }
    }

}
