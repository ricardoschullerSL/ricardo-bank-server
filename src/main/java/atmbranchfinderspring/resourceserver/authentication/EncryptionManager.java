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
public class EncryptionManager {

	private PEMParser pemParser;
	private PEMKeyPair pemKeyPair;
	private JcaPEMKeyConverter converter;
	private KeyPair keyPair;
	private ECPrivateKey privateKey;
	private ECPublicKey publicKey;
	private Algorithm algorithm;
	private JWTVerifier jwtVerifier;

	public EncryptionManager() {

		try {
			Security.addProvider(new BouncyCastleProvider());
			pemParser = new PEMParser(new InputStreamReader(getClass().getResourceAsStream("/ec256-key-pair.pem")));
			pemKeyPair = (PEMKeyPair) pemParser.readObject();
			converter = new JcaPEMKeyConverter();
			keyPair = converter.getKeyPair(pemKeyPair);
			pemParser.close();

			privateKey = (ECPrivateKey) keyPair.getPrivate();
			publicKey = (ECPublicKey) keyPair.getPublic();


			algorithm = Algorithm.ECDSA256(publicKey,privateKey);

			jwtVerifier = JWT.require(algorithm)
					.withIssuer("Open Banking")
					.build();

			// Use this part to print out an encoded JWT for registering client.
//            String testJWT = JWT.create().withIssuer("Open Banking")
//                    .withClaim("software_id","Scotty")
//                    .withClaim("aud","Scott Logic Bank")
//                    .withClaim("redirect_uri","http://localhost:8081/redirect")
//                    .withClaim("software_statement","testsoftwarestatement")
//                    .withJWTId("jwtId")
//                    .sign(algorithm);
//            System.out.println(testJWT);
		} catch (IOException e) {
			System.out.println(e);

		}
	}

	public JWTVerifier getJwtVerifier() {
		return jwtVerifier;
	}
}
