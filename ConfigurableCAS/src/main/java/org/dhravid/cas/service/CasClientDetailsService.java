package org.dhravid.cas.service;

import org.dhravid.cas.repository.CasClientDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

/**
 * @author Shankar Govindarajan
*/

@Service
public class CasClientDetailsService implements ClientDetailsService 
{
	@Autowired
	@Qualifier("DbClientRepo")
	private CasClientDetailsRepository clientDetailsRepository;

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException 
	{
		ClientDetails clientDetails = clientDetailsRepository.findByClientId(clientId);
		if(clientDetails == null)
		{
			throw new NoSuchClientException("No client with requested id: " + clientId);
		}		 
		return clientDetails;
	}
}
