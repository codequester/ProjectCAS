package org.dhravid.cas.service;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;


/**
 * @author Shankar Govindarajan
 *
 */

public class CasJwtEnhancer extends JwtAccessTokenConverter 
{
	public CasJwtEnhancer() throws Exception
	{
		super();
		setSigningKey("ABC123W");
		afterPropertiesSet();
	}
	
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) 
    {
    	//Refer the code sample below to add additional info to the token
    	/*    	
        Map<String, Object> additionalInfo = new HashMap<String, Object>();
        additionalInfo.putAll(accessToken.getAdditionalInformation());
        additionalInfo.put("companyId", "Z996");

        DefaultOAuth2AccessToken enhancedToken = new DefaultOAuth2AccessToken(accessToken);
        enhancedToken.setAdditionalInformation(additionalInfo);
        
        return super.enhance(enhancedToken, authentication);
    	 */  	
    	return super.enhance(accessToken, authentication)  ;
    }
}
