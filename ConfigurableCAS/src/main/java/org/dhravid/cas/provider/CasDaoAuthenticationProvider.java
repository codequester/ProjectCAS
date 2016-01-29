
package org.dhravid.cas.provider;

import org.dhravid.cas.service.CasUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author Shankar Govindarajan
*/

@Order(1)
@Component("DaoAuthentication")
public class CasDaoAuthenticationProvider implements AuthenticationProvider
{
	private DaoAuthenticationProvider daoAuthentcationProvider = new DaoAuthenticationProvider();
	
	@Autowired
	public CasDaoAuthenticationProvider(CasUserDetailsService casUserDetailsService)
	{
		daoAuthentcationProvider.setUserDetailsService(casUserDetailsService);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException 
	{
		return daoAuthentcationProvider.authenticate(authentication);
	}

	@Override
	public boolean supports(Class<?> authentication) 
	{
		return true;
	}

}
