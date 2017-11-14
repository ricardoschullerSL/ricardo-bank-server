package atmbranchfinderspring.resourceserver;

import atmbranchfinderspring.resourceserver.aspects.SecurityAspect;
import atmbranchfinderspring.resourceserver.aspects.SecurityHandler;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@EnableAutoConfiguration
@EnableTransactionManagement
public class AppConfig extends WebMvcConfigurerAdapter {


	@Autowired
	private AuthenticationManager authenticationManager;
//
//	@Bean
//	public SecurityAspect security() {
//		return new SecurityAspect(authenticationManager);
//	}

//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new SecurityHandler(authenticationManager)).addPathPatterns("/token/**");
//	}

	@Bean
	public SessionFactory sessionFactory(HibernateEntityManagerFactory hemf){
		return hemf.getSessionFactory();
	}

}
