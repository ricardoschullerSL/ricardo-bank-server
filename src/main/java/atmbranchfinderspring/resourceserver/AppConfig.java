package atmbranchfinderspring.resourceserver;

import atmbranchfinderspring.resourceserver.aspects.Security;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManagerImpl;
import atmbranchfinderspring.resourceserver.authentication.EncryptionManager;
import atmbranchfinderspring.resourceserver.authentication.PEMManagerImpl;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import atmbranchfinderspring.resourceserver.repos.AdminRepository;
import atmbranchfinderspring.resourceserver.repos.TPPClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AppConfig {


//	@Bean
//	public PEMManagerImpl pemManagerImpl() {
//		return new PEMManagerImpl();
//	}
//
//	@Bean
//	public EncryptionManager encryptionManager() {
//		return new EncryptionManager(pemManagerImpl());
//	}
//
//	@Bean
//	public AccessTokenRepository accessTokenRepository() {
//		return new AccessTokenRepository();
//	}
//
//	@Bean
//	public TPPClientRepository tppClientRepository() {
//		return new TPPClientRepository();
//	}
//
//	@Bean
//	public AdminRepository adminRepository() {
//		return new AdminRepository();
//	}
//
//	@Bean
//	public AuthenticationManagerImpl authenticationManager () {
//		return new AuthenticationManagerImpl(accessTokenRepository(), tppClientRepository(), adminRepository(), encryptionManager());
//	}

	@Autowired
	private AuthenticationManager authenticationManager;

	@Bean
	public Security security() {
		return new Security(authenticationManager);
	}
}
