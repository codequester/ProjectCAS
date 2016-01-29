package org.dhravid.cas.service;

import org.dhravid.cas.domain.CasUserDetails;
import org.dhravid.cas.repository.CasUserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Shankar Govindarajan
*/

@Service
public class CasUserDetailsService implements UserDetailsService 
{
	@Autowired
	@Qualifier("DbUserRepo")
	private CasUserDetailsRepository userDetailsRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		CasUserDetails userDetails = userDetailsRepository.findByUserId(username);
		if(userDetails!=null && userDetails.getUserId()!=null)
		{
			return new User(userDetails.getUserId(), userDetails.getPassword(), AuthorityUtils.createAuthorityList("USER"));
		}
		else
		{
			throw new UsernameNotFoundException("User ["+username+"] Not Found");
		}
	}
}
