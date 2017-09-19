package atmbranchfinderspring.resourceserver.authentication;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class EncryptionManager {


	private PEMManager pemManagerImp;
	private JWTVerifier jwtVerifier;

	@Autowired
	public EncryptionManager(PEMManager pemManagerImp) {
		this.pemManagerImp = pemManagerImp;

	}


	public PEMManager getPemManagerImp() {
		return pemManagerImp;
	}

	public Algorithm getAlgorithm() {
		return pemManagerImp.getAlgorithm();
	}

	public JWTVerifier getJwtVerifier() {
		return pemManagerImp.getJwtVerifier();
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
