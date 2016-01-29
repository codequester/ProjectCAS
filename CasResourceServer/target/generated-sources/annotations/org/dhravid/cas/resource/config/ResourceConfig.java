package org.dhravid.cas.resource.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
@Configuration
@ComponentScan(basePackages={"org.dhravid.*"})
public class ResourceConfig extends AbstractResourceConfig
{
	@Autowired
	private ResourceServerTokenServices casTokenService;
   @Override
   public void configure(ResourceServerSecurityConfigurer resources) throws Exception
   {
   	resources.resourceId("myresource1");
   	resources.tokenServices(casTokenService);
   }
   @Override
   public void configure(HttpSecurity http) throws Exception
   {
   	http.authorizeRequests().anyRequest().authenticated();
   }
}
