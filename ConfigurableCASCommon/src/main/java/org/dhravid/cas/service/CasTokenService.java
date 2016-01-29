package org.dhravid.cas.service;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.dhravid.cas.util.CasUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Shankar Govindarajan
*/

@Service
public class CasTokenService extends DefaultTokenServices implements AuthServerTokenService,ResourceServerTokenServices
{
	@Autowired
	private HttpServletRequest request;
	
	//@Autowired
	//private ClientDetailsService casClientDetailsService;
	
	@Autowired
	private DataSource dataSource;
	
	public CasTokenService()
	{
		super();
		this.setTokenStore(new InMemoryTokenStore());
		this.setSupportRefreshToken(true);		
	}

	@Transactional
	public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException
	{
		setTokenStoreForRequest();
		//setClientDetailsService(casClientDetailsService);
		return super.createAccessToken(authentication);
	}
	
	private void setTokenStoreForRequest()
	{
		if(isDBTokenRequested())
		{
			this.setTokenStore(new JdbcTokenStore(dataSource));
			this.setTokenEnhancer(null);
		}
		else
		{
			JwtAccessTokenConverter tokenEnhancer = null;
			try {
				tokenEnhancer = new CasJwtEnhancer();
			} catch (Exception e) {
				e.printStackTrace();
			}
			TokenStore defaultTokenStore = new JwtTokenStore(tokenEnhancer);
			this.setTokenStore(defaultTokenStore);
			this.setTokenEnhancer(tokenEnhancer);
		}
	}

	private boolean isDBTokenRequested()
	{
		String dbTokenParamVal = request.getParameter(CasUtil.REQ_PARAM_DB_TOKEN);
		return "Y".equalsIgnoreCase(dbTokenParamVal) || "TRUE".equalsIgnoreCase(dbTokenParamVal);
	}
	
	public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException
	{
		OAuth2Authentication oauth2Authentication = null;
		try
		{
			oauth2Authentication = loadAuthenticationFromJwtToken(accessToken);
		}
		catch(InvalidTokenException e)
		{
			if(e.getMessage().startsWith("Access token expired"))
			{
				throw new InvalidTokenException("Access token expired: " + accessToken);
			}
			else
			{
				//logger.warn("Not A JWT Token. Attempt to load token from JDBC Store");
			}
		}
		if(oauth2Authentication == null)
		{
			oauth2Authentication = loadAuthenticationFromJdbcToken(accessToken);
			if(oauth2Authentication == null)
			{
				throw new InvalidTokenException("Invalid access token: " + accessToken);
			}
		}
/*		if (casClientDetailsService != null) 
		{
			String clientId = oauth2Authentication.getOAuth2Request().getClientId();
			try 
			{
				casClientDetailsService.loadClientByClientId(clientId);
			}
			catch (ClientRegistrationException e) 
			{
				throw new InvalidTokenException("Client not valid: " + clientId, e);
			}
		}
*/		return oauth2Authentication;

	}

	public OAuth2AccessToken readAccessToken(String accessTokenValue)
	{
		JwtAccessTokenConverter tokenEnhancer = null;
		try {
			tokenEnhancer = new CasJwtEnhancer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		TokenStore tokenStore = new JwtTokenStore(tokenEnhancer);
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
		if(accessToken!=null)
		{
			return accessToken;
		}
		else
		{
			tokenStore = new JdbcTokenStore(dataSource);
			return tokenStore.readAccessToken(accessTokenValue);
		}
	}
	

	private OAuth2Authentication loadAuthenticationFromJwtToken(String accessTokenValue)  throws AuthenticationException, InvalidTokenException
	{
		OAuth2Authentication oauth2Authentication = null;
		JwtAccessTokenConverter tokenEnhancer = null;
		try {
			tokenEnhancer = new CasJwtEnhancer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		TokenStore tokenStore = new JwtTokenStore(tokenEnhancer);
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
		if (accessToken != null) 
		{
			if (accessToken.isExpired()) 
			{
				throw new InvalidTokenException("Access token expired: " + accessToken);
			}
			oauth2Authentication = tokenStore.readAuthentication(accessToken);
		}
		return oauth2Authentication;
	}
	
	private OAuth2Authentication loadAuthenticationFromJdbcToken(String accessTokenValue)  throws AuthenticationException, InvalidTokenException
	{
		OAuth2Authentication oauth2Authentication = null;
		TokenStore tokenStore = new JdbcTokenStore(dataSource);
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
		if (accessToken != null) 
		{
			if (accessToken.isExpired()) 
			{
				throw new InvalidTokenException("Access token expired: " + accessToken);
			}
			oauth2Authentication = tokenStore.readAuthentication(accessToken);
		}
		return oauth2Authentication;
	}
	
}
