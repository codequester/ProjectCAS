package org.dhravid.cas.mongo.repository;

import org.dhravid.cas.mongo.domain.MongoUserDetails;
import org.dhravid.cas.repository.CasUserDetailsRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Shankar Govindarajan
*/

@Repository("MongoUserRepo")
public interface MongoUserDetailsRepository extends MongoRepository<MongoUserDetails, String>, CasUserDetailsRepository 
{
	public MongoUserDetails findByUserId(String userId);
}
