package org.dhravid.cas.db.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.dhravid.cas.domain.CasUserDetails;

import lombok.Data;

/**
 * @author Shankar Govindarajan
*/

@Entity
@Table(name="users")
public @Data class DbUserDetails implements CasUserDetails
{
	@Id
	private String userId;
	private String password;
	private String companyId;
}
