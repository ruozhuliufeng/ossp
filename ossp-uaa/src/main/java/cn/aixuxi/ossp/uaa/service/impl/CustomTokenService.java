package cn.aixuxi.ossp.uaa.service.impl;

import cn.aixuxi.ossp.common.constant.SecurityConstants;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * 重写DefauoltTokenServices，实现登录同应用同账号互踢
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 10:32
 **/
public class CustomTokenService extends DefaultTokenServices {

    private TokenStore tokenStore;
    private TokenEnhancer accessTokenEnhancer;
    private AuthenticationManager authenticationManager;
    private boolean supportRefreshToken = false;
    private boolean reuseRefreshToken = true;
    /**
     * 是否登录同应用同账号互踢
     */
    private boolean isSingleLogin;

    public CustomTokenService(boolean isSingleLogin){
        this.isSingleLogin = isSingleLogin;
    }


    @Override
    @Transactional
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(authentication);
        OAuth2RefreshToken refreshToken = null;
        if (existingAccessToken != null){
            if (isSingleLogin){
                if (existingAccessToken.getRefreshToken() != null){
                    tokenStore.removeRefreshToken(existingAccessToken.getRefreshToken());
                }
                tokenStore.removeAccessToken(existingAccessToken);
            }else if(existingAccessToken.isExpired()){
                if (existingAccessToken.getRefreshToken() != null){
                    refreshToken = existingAccessToken.getRefreshToken();
                    // 当访问令牌被删除时，令牌存储可以删除刷新令牌，但我们想确定......
                    tokenStore.removeRefreshToken(refreshToken);
                }
                tokenStore.removeAccessToken(existingAccessToken);
            }else {
                // oidc每次授权都刷新id_token
                existingAccessToken = refreshIdToken(existingAccessToken,authentication);
                // 重新存储
                tokenStore.storeAccessToken(existingAccessToken,authentication);
                return existingAccessToken;
            }
        }
        // 如果没有与过期的访问令牌相关联的现有令牌，则仅创建新的刷新令牌
        // 客户端可能持有现有的刷新令牌，因此我们在旧访问令牌过期的情况下重新使用它.
        if (refreshToken == null){
            refreshToken = createRefreshToken(authentication);
        }else if (refreshToken instanceof ExpiringOAuth2RefreshToken){
            ExpiringOAuth2RefreshToken expiring = (ExpiringOAuth2RefreshToken) refreshToken;
            if (System.currentTimeMillis() > expiring.getExpiration().getTime()){
                refreshToken = createRefreshToken(authentication);
            }
        }
        OAuth2AccessToken accessToken = createAccessToken(authentication,refreshToken);
        tokenStore.storeAccessToken(accessToken,authentication);
        refreshToken = accessToken.getRefreshToken();
        if (refreshToken != null){
            tokenStore.storeRefreshToken(refreshToken,authentication);
        }
        return accessToken;
    }

    /**
     * OIDC 每次授权都刷新id_token
     * @param token 已存在的token
     * @param authentication 认证信息
     * @return OAuth2AccessToken
     */
    private OAuth2AccessToken refreshIdToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Set<String> responseTypes = authentication.getOAuth2Request().getResponseTypes();
        if (accessTokenEnhancer != null && responseTypes.contains(SecurityConstants.ID_TOKEN)){
            return accessTokenEnhancer.enhance(token,authentication);
        }
        return token;
    }

    private OAuth2RefreshToken createRefreshToken(OAuth2Authentication authentication){
        if (!isSupportRefreshToken(authentication.getOAuth2Request())){
            return null;
        }
        int validitySeconds = getRefreshTokenValiditySeconds(authentication.getOAuth2Request());
        String value = UUID.randomUUID().toString();
        if (validitySeconds>0){
            return new DefaultExpiringOAuth2RefreshToken(value,new Date(System.currentTimeMillis()
                    + (validitySeconds * 1000L)));
        }
        return new DefaultOAuth2RefreshToken(value);
    }

    private OAuth2AccessToken createAccessToken(OAuth2Authentication authentication,OAuth2RefreshToken refreshToken){
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
        int validitySeconds = getAccessTokenValiditySeconds(authentication.getOAuth2Request());
        if (validitySeconds > 0){
            token.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
        }
        token.setRefreshToken(refreshToken);
        token.setScope(authentication.getOAuth2Request().getScope());
        return accessTokenEnhancer != null ? accessTokenEnhancer.enhance(token,authentication) : token;
    }

    /**
     * 令牌存储的持久化策略。
     *
     * @param tokenStore the store for access and refresh tokens.
     */
    @Override
    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
        super.setTokenStore(tokenStore);
    }

    /**
     * 一个访问令牌增强器，将在新令牌保存在令牌存储中之前应用于新令牌。
     *
     * @param accessTokenEnhancer the access token enhancer to set
     */
    @Override
    public void setTokenEnhancer(TokenEnhancer accessTokenEnhancer) {
        this.accessTokenEnhancer = accessTokenEnhancer;
        super.setTokenEnhancer(accessTokenEnhancer);
    }

    @Override
    @Transactional(noRollbackFor = {InvalidTokenException.class, InvalidGrantException.class})
    public OAuth2AccessToken refreshAccessToken(String refreshTokenValue,
                                                TokenRequest tokenRequest) throws AuthenticationException {
        if (!supportRefreshToken){
            throw new InvalidGrantException("刷新Token无效：{}"+refreshTokenValue);
        }
        OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(refreshTokenValue);
        if (refreshToken == null){
            throw new InvalidGrantException("刷新Token无效：{}"+refreshTokenValue);
        }
        OAuth2Authentication authentication = tokenStore.readAuthenticationForRefreshToken(refreshToken);
        if (this.authenticationManager!=null && !authentication.isClientOnly()){
            AbstractAuthenticationToken userAuthentication = (AbstractAuthenticationToken) authentication.getUserAuthentication();
            Object userDetails = userAuthentication.getDetails();
            // 客户端也许已经授权，但用户信息可能是之前的授权信息,所以在此重新给用户信息重新授权
            Authentication user = new PreAuthenticatedAuthenticationToken(userAuthentication,
                    "",authentication.getAuthorities());
            user = authenticationManager.authenticate(user);
            // 保存账号类型
            ((PreAuthenticatedAuthenticationToken)user).setDetails(userDetails);
            Object details = authentication.getDetails();
            authentication = new OAuth2Authentication(authentication.getOAuth2Request(),user);
            authentication.setDetails(details);
        }
        String clientId = authentication.getOAuth2Request().getClientId();
        if (clientId == null || !clientId.equals(tokenRequest.getClientId())) {
            throw new InvalidGrantException("刷新Token不符合此客户端"+refreshTokenValue);
        }
        // 清除认证Token
        tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);
        if (isExpired(refreshToken)){
            tokenStore.removeRefreshToken(refreshToken);
            throw new InvalidTokenException("刷新Token已过期"+refreshToken);
        }
        authentication = createRefreshedAuthentication(authentication,tokenRequest);
        if (!reuseRefreshToken){
            tokenStore.removeRefreshToken(refreshToken);
            refreshToken = createRefreshToken(authentication);
        }
        OAuth2AccessToken accessToken = createAccessToken(authentication,refreshToken);
        tokenStore.storeAccessToken(accessToken,authentication);
        if (!reuseRefreshToken){
            tokenStore.storeRefreshToken(accessToken.getRefreshToken(),authentication);
        }
        return accessToken;
    }

    private OAuth2Authentication createRefreshedAuthentication(OAuth2Authentication authentication,TokenRequest tokenRequest){
        OAuth2Authentication narrowed;
        Set<String> scope = tokenRequest.getScope();
        OAuth2Request clientAuth = authentication.getOAuth2Request().refresh(tokenRequest);
        if (scope != null && !scope.isEmpty()){
            Set<String> originalScope = clientAuth.getScope();
            if (originalScope == null || !originalScope.containsAll(scope)){
                throw new InvalidScopeException("Unable to narrow the scope of the client authentication to " + scope
                        + ".", originalScope);
            }else {
                clientAuth = clientAuth.narrowScope(scope);
            }
        }
        narrowed = new OAuth2Authentication(clientAuth,authentication.getUserAuthentication());
        return narrowed;
    }

    /**
     * An authentication manager that will be used (if provided) to check the user authentication when a token is
     * refreshed.
     *
     * @param authenticationManager the authenticationManager to set
     */
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setAuthenticationManager(authenticationManager);
    }

    /**
     * Whether to support the refresh token.
     *
     * @param supportRefreshToken Whether to support the refresh token.
     */
    @Override
    public void setSupportRefreshToken(boolean supportRefreshToken) {
        this.supportRefreshToken = supportRefreshToken;
        super.setSupportRefreshToken(supportRefreshToken);
    }

    /**
     * Whether to reuse refresh tokens (until expired).
     *
     * @param reuseRefreshToken Whether to reuse refresh tokens (until expired).
     */
    @Override
    public void setReuseRefreshToken(boolean reuseRefreshToken) {
        this.reuseRefreshToken = reuseRefreshToken;
        super.setReuseRefreshToken(reuseRefreshToken);
    }
}
