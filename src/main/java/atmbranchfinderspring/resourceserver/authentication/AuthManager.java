package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import atmbranchfinderspring.resourceserver.repos.TPPClientRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

/**
 * @author Ricardo Schuller
 * @version 0.1.0
 * @since 0.1.0
 */


@Component
public class AuthManager implements AuthenticationManager {

    private PEMParser pemParser;
    private PEMKeyPair pemKeyPair;
    private JcaPEMKeyConverter converter;
    private KeyPair keyPair;
    private ECPrivateKey privateKey;
    private ECPublicKey publicKey;
    private Algorithm algorithm;
    private JWTVerifier jwtVerifier;
    private AccessTokenRepository accessTokenRepository;
    private TPPClientRepository tppClientRepository;

    @Autowired
    public AuthManager(AccessTokenRepository accessTokenRepository, TPPClientRepository tppClientRepository) {
        try {
            this.tppClientRepository = tppClientRepository;
            this.accessTokenRepository = accessTokenRepository;
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

    public PEMParser getPemParser() {
        return pemParser;
    }

    public PEMKeyPair getPemKeyPair() {
        return pemKeyPair;
    }

    public JcaPEMKeyConverter getConverter() {
        return converter;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public ECPrivateKey getPrivateKey() {
        return privateKey;
    }

    public ECPublicKey getPublicKey() {
        return publicKey;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public JWTVerifier getJWTVerifier() {
        return jwtVerifier;
    }

    public Boolean isAccessTokenValid(String token) {
        if (accessTokenRepository.contains(token)) {
            accessTokenRepository.delete(token);
            return true;
        } else {
            return false;
        }
    }

    public Boolean areCredentialsCorrect(String clientId, String clientSecret) {
        TPPClient client = (TPPClient) tppClientRepository.get(clientId);
        return !(client == null) && client.getCredentials().getClientSecret().equals(clientSecret);
    }
}
