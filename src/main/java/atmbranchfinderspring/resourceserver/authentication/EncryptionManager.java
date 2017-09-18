package atmbranchfinderspring.resourceserver.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class EncryptionManager {


	private PEMManager pemManager;
	private JWTVerifier jwtVerifier;

	@Autowired
	public EncryptionManager(PEMManager pemManager) {
		this.pemManager = pemManager;

	}


	public PEMManager getPemManager() {
		return pemManager;
	}

	public Algorithm getAlgorithm() {
		return pemManager.getAlgorithm();
	}

	public JWTVerifier getJwtVerifier() {
		return jwtVerifier;
	}

	public byte[] SHA256(String string) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return digest.digest(string.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
}
