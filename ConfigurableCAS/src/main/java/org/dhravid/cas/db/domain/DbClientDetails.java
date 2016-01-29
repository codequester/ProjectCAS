package org.dhravid.cas.db.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import lombok.Data;

/**
 * @author Shankar Govindarajan
*/


@Entity
@Table(name="clients")
public @Data class DbClientDetails implements ClientDetails
{	
	private static final long serialVersionUID = -6800426465795935775L;
	@Id
	private String clientId;
	private String clientSecret;
	private Integer accessTokenValiditySeconds;
	private Integer refreshTokenValiditySeconds;
	@Transient
	private Set<String> resourceIds;
	@Transient
	private Set<String> authorizedGrantTypes;
	@Transient
	private Set<String> scope;
	@Transient
	private Set<String> registeredRedirectUri;
	@Transient
	private Set<String> autoApproveScopes;
	@Transient
	private Collection<String> authorities;
	    
	@ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="client_attributes", joinColumns={@JoinColumn(name="clientId")})
	private Collection<ClientAttribute> clientAttributes;
	
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
	
	@PostLoad
	public void initAttributes()
	{
		resourceIds = getAttributesByType("REG_RESOURCE_ID");
		authorizedGrantTypes = getAttributesByType("GRANT_TYPE");
		scope = getAttributesByType("SCOPE_TYPE");
		registeredRedirectUri = getAttributesByType("REG_REDIRECT_URI");
		autoApproveScopes = getAttributesByType("AUTO_APPROVE_SCOPE_TYPE");
		authorities = getAttributesByType("AUTH_TYPE");
	}
	
	private Set<String> getAttributesByType(String attributeType)
	{
		Set<String> attributesByType = new HashSet<String>();
		for(ClientAttribute attribute:clientAttributes)
		{
			if(attributeType.equals(attribute.getClientAttributeType()))
			{
				attributesByType.add(attribute.getClientAttributeValue());
			}
		}
		return attributesByType;
	}
}
