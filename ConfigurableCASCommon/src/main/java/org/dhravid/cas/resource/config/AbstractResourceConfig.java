package org.dhravid.cas.resource.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@ComponentScan(basePackages={"org.dhravid.*"})
public abstract class AbstractResourceConfig extends ResourceServerConfigurerAdapter 
{

}
