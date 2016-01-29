package org.dhravid.cas.config;

import java.util.List;

import org.dhravid.cas.service.AuthServerTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;

/**
 * @author Shankar Govindarajan
*/

@Configuration
@EnableAutoConfiguration
@EnableAuthorizationServer
@ComponentScan(basePackages={"org.dhravid.cas.*"})
@EnableMongoRepositories("org.dhravid.cas.mongo.repository")
@EnableJpaRepositories("org.dhravid.cas.db.repository")
@EntityScan(basePackages={"org.dhravid.cas.db.domain"})
public class CasApplicationConfig implements AuthorizationServerConfigurer 
{

	@Autowired
	private ClientDetailsService casClientDetailsService;
	
	@Autowired
	private AuthServerTokenService casTokenService;
	
	@Autowired
	private List<AuthenticationProvider> configuredAutheticationProviders;
	
/*	@Bean
	public AuthenticationProvider casAutheticationProvider()
	{
		return new CasAuthenticationProvider(configuredAutheticationProviders);
	}
*/	
	@Bean
	public AuthenticationManager authenticationManager() throws Exception 
	{
		return new ProviderManager(configuredAutheticationProviders);
	}	
	
    @Bean
    @ConfigurationProperties(prefix="ldap.contextSource")
    public LdapContextSource contextSource() 
    {
        LdapContextSource contextSource = new LdapContextSource();
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate(ContextSource contextSource) 
    {
        return new LdapTemplate(contextSource);
    }	
	
	public static void main(String[] args)
	{
		SpringApplication.run(CasApplicationConfig.class, args);
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception 
	{
		security.allowFormAuthenticationForClients();
	}
	
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception 
	{
		clients.withClientDetails(casClientDetailsService);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception 
	{
		endpoints.authenticationManager(authenticationManager()).tokenServices(casTokenService);		
	}
}
