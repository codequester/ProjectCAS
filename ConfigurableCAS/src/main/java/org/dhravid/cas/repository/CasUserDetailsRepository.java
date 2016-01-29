package org.dhravid.cas.repository;

import org.dhravid.cas.domain.CasUserDetails;

/**
 * @author Shankar Govindarajan
*/

public interface CasUserDetailsRepository
{
	public CasUserDetails findByUserId(String userId);
}
