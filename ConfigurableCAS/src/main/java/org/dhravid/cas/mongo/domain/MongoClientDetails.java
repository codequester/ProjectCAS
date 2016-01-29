package org.dhravid.cas.mongo.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import lombok.Data;

/**
 * @author Shankar Govindarajan
*/


@Document(collection = "clients")
public @Data class MongoClientDetails implements ClientDetails
{
	private static final long serialVersionUID = 996482934837174001L;
	@Id
	private String id;
	private String clientId;
	private String clientSecret;
	private Integer accessTokenValiditySeconds;
	private Integer refreshTokenValiditySeconds;
	private Set<String> resourceIds;
	private Set<String> authorizedGrantTypes;	
	private Set<String> scope;
	private Set<String> registeredRedirectUri;
	private Set<String> autoApproveScopes;
	private Collection<String> authorities;

	@Override
	public boolean isSecretRequired() 
	{
		return this.clientSecret != null;
	}
	@Override
	public boolean isScoped() 
	{
		return this.scope != null && !this.scope.isEmpty();
	}
	@Override
	public boolean isAutoApprove(String scope) 
	{
		if (autoApproveScopes == null) {
			return false;
		}
		for (String auto : autoApproveScopes) {
			if (auto.equals("true") || scope.matches(auto)) {
				return true;
			}
		}
		return false;
	}
	@Override
	public Map<String, Object> getAdditionalInformation() 
	{
		return null;
	}
	
	@Override
	public Collection<GrantedAuthority> getAuthorities()
	{
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		for(String authority:authorities)
		{
			grantedAuthorities.add(new SimpleGrantedAuthority(authority));
		}
		return grantedAuthorities;
	}
}
