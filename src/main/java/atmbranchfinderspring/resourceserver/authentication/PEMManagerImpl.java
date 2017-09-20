package atmbranchfinderspring.resourceserver.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

@Component
public class PEMManagerImpl implements PEMManager {
	private PEMParser pemParser;
	private PEMKeyPair pemKeyPair;
	private JcaPEMKeyConverter converter;
	private KeyPair keyPair;
	private ECPrivateKey privateKey;
	private ECPublicKey publicKey;
	private Algorithm algorithm;
	private JWTVerifier jwtVerifier;

	public ECPrivateKey getPrivateKey() {
		return privateKey;
	}

	public ECPublicKey getPublicKey() {
		return publicKey;
	}

	public PEMManagerImpl() {
		try {
			Security.addProvider(new BouncyCastleProvider());
			pemParser = new PEMParser(new InputStreamReader(getClass().getResourceAsStream("/ec256-key-pair.pem")));
			pemKeyPair = (PEMKeyPair) pemParser.readObject();
			converter = new JcaPEMKeyConverter();
			keyPair = converter.getKeyPair(pemKeyPair);
			pemParser.close();

			privateKey = (ECPrivateKey) keyPair.getPrivate();
			publicKey = (ECPublicKey) keyPair.getPublic();
			this.algorithm = Algorithm.ECDSA256(publicKey, privateKey);
			this.jwtVerifier = JWT.require(algorithm)
					.withIssuer("Open Banking")
					.build();



		} catch (IOException e) {
			System.out.println(e);

		}
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public JWTVerifier getJwtVerifier() {
		return jwtVerifier;
	}

	public void setKeyPair(ECPrivateKey privateKey, ECPublicKey publicKey) {
		this.privateKey = privateKey;
		this.publicKey = publicKey;

	}

	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public void recalculateAlgorithm() {
		this.algorithm = Algorithm.ECDSA256(this.publicKey, this.privateKey);
	}

	public void setKeysAndRecalculateAlgorithm(ECPublicKey publicKey, ECPrivateKey privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.algorithm = Algorithm.ECDSA256(publicKey, privateKey);
	}
}
