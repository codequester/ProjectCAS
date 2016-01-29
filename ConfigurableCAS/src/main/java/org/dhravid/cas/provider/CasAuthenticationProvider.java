package org.dhravid.cas.provider;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;



/*
 * This class must be implmented at a later point of time to perform voter based logic.
 */

/**
 * @author Shankar Govindarajan
 *
 */
public class CasAuthenticationProvider implements AuthenticationManager
{

	private List<AuthenticationProvider> configuredAutheticationProviders;
	
	public CasAuthenticationProvider(List<AuthenticationProvider> configuredAutheticationProviders)
	{
		this.configuredAutheticationProviders = configuredAutheticationProviders;
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException 
	{
		for(AuthenticationProvider authProvider: configuredAutheticationProviders)
		{
			authentication = authProvider.authenticate(authentication);
			if(authentication.isAuthenticated())
			{
				break;
			}
		}
		return authentication;
	}
}
