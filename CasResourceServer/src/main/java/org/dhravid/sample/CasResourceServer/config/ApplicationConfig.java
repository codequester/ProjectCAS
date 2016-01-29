package org.dhravid.sample.CasResourceServer.config;

import org.dhravid.cas.resource.config.EnableProtection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Shankar Govindarajan
 *
 */
@Configuration
@EnableAutoConfiguration
@EnableProtection("myresource1")
@RestController
public class ApplicationConfig 
{
	public static void main(String[] args)
	{
		SpringApplication.run(ApplicationConfig.class, args);
	}

	@RequestMapping("/saysample")
	public String saySample()
	{
		return "HELLO SAMPLE CAS RESOURCE ! ! !";
	}
	
}
