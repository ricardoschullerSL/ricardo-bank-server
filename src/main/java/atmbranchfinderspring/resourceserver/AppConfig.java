package atmbranchfinderspring.resourceserver;

import atmbranchfinderspring.resourceserver.aspects.SecurityAspect;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@EnableAutoConfiguration
public class AppConfig extends WebMvcConfigurerAdapter {


	@Autowired
	private AuthenticationManager authenticationManager;

	@Bean
	public SecurityAspect security() {
		return new SecurityAspect(authenticationManager);
	}


}
