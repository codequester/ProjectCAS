package org.dhravid.cas.repository;

import org.springframework.security.oauth2.provider.ClientDetails;

/**
 * @author Shankar Govindarajan
*/

public interface CasClientDetailsRepository 
{
	public ClientDetails findByClientId(String clientId);
}
