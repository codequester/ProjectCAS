package org.dhravid.cas.db.domain;

import javax.persistence.Embeddable;

import lombok.Data;

/**
 * @author Shankar Govindarajan
*/

@Embeddable
public @Data class ClientAttribute
{	
	private String clientAttributeValue;
	private String clientAttributeType;	
}
