package org.dhravid.cas.db.repository;

import org.dhravid.cas.db.domain.DbClientDetails;
import org.dhravid.cas.repository.CasClientDetailsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Shankar Govindarajan
*/

@Repository("DbClientRepo")
public interface DbClientDetailsRepository extends JpaRepository<DbClientDetails, String>, CasClientDetailsRepository
{
	public DbClientDetails findByClientId(String clientId);
}
