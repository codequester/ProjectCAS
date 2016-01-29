package org.dhravid.cas.mongo.repository;

import org.dhravid.cas.mongo.domain.MongoClientDetails;
import org.dhravid.cas.repository.CasClientDetailsRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Shankar Govindarajan
*/

@Repository("MongoClientRepo")
public interface MongoClientDetailsRepository extends MongoRepository<MongoClientDetails, String> , CasClientDetailsRepository
{
	public MongoClientDetails findByClientId(String clientId);
}
