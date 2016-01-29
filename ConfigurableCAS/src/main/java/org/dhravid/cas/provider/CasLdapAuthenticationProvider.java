package org.dhravid.cas.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.stereotype.Component;

/**
 * @author Shankar Govindarajan
*/

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component("LdapAuthentication")
public class CasLdapAuthenticationProvider implements AuthenticationProvider
{
	private LdapAuthenticationProvider ldapAuthProvider;
	
	@Autowired
	public CasLdapAuthenticationProvider(LdapContextSource contextSource)
	{
		BindAuthenticator ldapAuthenticator = new BindAuthenticator(contextSource);
		String[] userDnPattern = {"cn={0},ou=users"};
		ldapAuthenticator.setUserDnPatterns(userDnPattern);
		ldapAuthProvider = new LdapAuthenticationProvider(ldapAuthenticator);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException 
	{
		return ldapAuthProvider.authenticate(authentication);
	}

	@Override
	public boolean supports(Class<?> authentication) 
	{
		return true;
	}
}
