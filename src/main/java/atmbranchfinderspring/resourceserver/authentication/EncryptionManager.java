package atmbranchfinderspring.resourceserver.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EncryptionManager {


	private PEMManager pemManager;
	private Algorithm algorithm;
	private JWTVerifier jwtVerifier;

	@Autowired
	public EncryptionManager(PEMManager pemManager) {
		this.pemManager = pemManager;

	}


	public PEMManager getPemManager() {
		return pemManager;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public JWTVerifier getJwtVerifier() {
		return jwtVerifier;
	}
}
