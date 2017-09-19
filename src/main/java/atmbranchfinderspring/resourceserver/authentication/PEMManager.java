package atmbranchfinderspring.resourceserver.authentication;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

public interface PEMManager {
	JWTVerifier getJwtVerifier();
	Algorithm getAlgorithm();
	ECPrivateKey getPrivateKey();
	ECPublicKey getPublicKey();
}
