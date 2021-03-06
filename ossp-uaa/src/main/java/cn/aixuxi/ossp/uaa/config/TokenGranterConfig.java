package cn.aixuxi.ossp.uaa.config;

import cn.aixuxi.ossp.uaa.granter.MobilePwdGranter;
import cn.aixuxi.ossp.uaa.granter.OpenIdGranter;
import cn.aixuxi.ossp.uaa.granter.PwdImgCodeGranter;
import cn.aixuxi.ossp.uaa.service.IValidateCodeService;
import cn.aixuxi.ossp.uaa.service.impl.CustomTokenService;
import cn.aixuxi.ossp.uaa.service.impl.UserDetailServiceFactory;
import cn.aixuxi.ossp.uaa.service.impl.UserDetailsByNameServiceFactoryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Token?????????????????????
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 16:05
 **/
@Configuration
public class TokenGranterConfig {
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private UserDetailServiceFactory userDetailServiceFactory;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenStore tokenStore;
    @Autowired(required = false)
    private List<TokenEnhancer> tokenEnhancer;
    @Autowired
    private IValidateCodeService validateCodeService;
    @Autowired
    private RandomValueAuthorizationCodeServices authorizationCodeServices;

    private boolean reuseRefreshToken = true;

    private AuthorizationServerTokenServices tokenServices;

    private TokenGranter tokenGranter;

    /**
     * ?????????????????????????????????
     */
    @Value("${ossp.uaa.isSingleLogin:false}")
    private boolean isSingleLogin;

    /**
     * ????????????
     */
    @Bean
    @ConditionalOnMissingBean
    public TokenGranter tokenGranter(DefaultTokenServices tokenServices) {
        if (tokenGranter == null) {
            tokenGranter = new TokenGranter() {
                private CompositeTokenGranter delegate;

                @Override
                public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
                    if (delegate == null) {
                        delegate = new CompositeTokenGranter(getAllTokenGranters(tokenServices));
                    }
                    return delegate.grant(grantType, tokenRequest);
                }
            };
        }
        return tokenGranter;
    }

    /**
     * ??????????????????????????????5?????????+??????????????????
     */
    private List<TokenGranter> getAllTokenGranters(DefaultTokenServices tokenServices) {
        AuthorizationCodeServices authorizationCodeServices = authorizationCodeServices();
        OAuth2RequestFactory requestFactory = requestFactory();
        // ???????????????????????????
        List<TokenGranter> tokenGranters = getDefaultTokenGranters(tokenServices, authorizationCodeServices, requestFactory);
        if (authenticationManager != null) {
            // ????????????????????????????????????
            tokenGranters.add(new PwdImgCodeGranter(authenticationManager, tokenServices, clientDetailsService, requestFactory, validateCodeService));
            // ??????openId??????
            tokenGranters.add(new OpenIdGranter(tokenServices, clientDetailsService, requestFactory, authenticationManager));
            // ????????????????????????????????????
            tokenGranters.add(new MobilePwdGranter(authenticationManager,tokenServices,clientDetailsService,requestFactory));

        }
        return tokenGranters;
    }

    /**
     * ???????????????????????????
     */
    private List<TokenGranter> getDefaultTokenGranters(AuthorizationServerTokenServices tokenServices,
                                                       AuthorizationCodeServices authorizationCodeServices,
                                                       OAuth2RequestFactory requestFactory) {
        List<TokenGranter> tokenGranters = new ArrayList<>();
        // ?????????????????????
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices,authorizationCodeServices,clientDetailsService,requestFactory));
        // ????????????????????????
        tokenGranters.add(new RefreshTokenGranter(tokenServices,clientDetailsService,requestFactory));
        // ????????????????????????
        tokenGranters.add(new ImplicitTokenGranter(tokenServices,clientDetailsService,requestFactory));
        // ?????????????????????
        tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices,clientDetailsService,requestFactory));
        if (authenticationManager != null){
            // ??????????????????
            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager,tokenServices,clientDetailsService,requestFactory));
        }
        return tokenGranters;
    }


    private AuthorizationServerTokenServices tokenServices() {
        if (tokenServices != null) {
            return tokenServices;
        }
        this.tokenServices = createDefaultTokenServices();
        return tokenServices;
    }

    private AuthorizationCodeServices authorizationCodeServices(){
        if (authorizationCodeServices == null){
            authorizationCodeServices = new InMemoryAuthorizationCodeServices();
        }
        return authorizationCodeServices;
    }

    private OAuth2RequestFactory requestFactory(){
        return new DefaultOAuth2RequestFactory(clientDetailsService);
    }

    @Bean
    @ConditionalOnMissingBean
    protected DefaultTokenServices createDefaultTokenServices() {
        DefaultTokenServices tokenServices = new CustomTokenService(isSingleLogin);
        tokenServices.setTokenStore(tokenStore);
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(reuseRefreshToken);
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setTokenEnhancer(tokenEnhancer());
        addUserDetailsService(tokenServices);
        return tokenServices;
    }


    private TokenEnhancer tokenEnhancer() {
        if (tokenEnhancer != null) {
            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            tokenEnhancerChain.setTokenEnhancers(tokenEnhancer);
            return tokenEnhancerChain;
        }
        return null;
    }

    private void addUserDetailsService(DefaultTokenServices tokenServices) {
        if (userDetailServiceFactory != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceFactoryWrapper<>(this.userDetailServiceFactory));
            tokenServices.setAuthenticationManager(new ProviderManager(Collections.singletonList(provider)));
        }
    }
}
