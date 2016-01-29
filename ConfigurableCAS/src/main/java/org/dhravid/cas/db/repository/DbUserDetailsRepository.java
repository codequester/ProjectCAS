package org.dhravid.cas.db.repository;

import org.dhravid.cas.db.domain.DbUserDetails;
import org.dhravid.cas.repository.CasUserDetailsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Shankar Govindarajan
*/

@Repository("DbUserRepo")
public interface DbUserDetailsRepository extends JpaRepository<DbUserDetails, String>, CasUserDetailsRepository
{
	public DbUserDetails findByUserId(String userId);
}
