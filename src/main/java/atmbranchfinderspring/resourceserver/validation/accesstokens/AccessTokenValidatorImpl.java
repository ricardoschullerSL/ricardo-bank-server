package atmbranchfinderspring.resourceserver.validation.accesstokens;

import atmbranchfinderspring.resourceserver.models.AccessToken;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccessTokenValidatorImpl implements AccessTokenValidator {

	private AccessTokenRepository accessTokenRepository;

	@Autowired
	public AccessTokenValidatorImpl(AccessTokenRepository accessTokenRepository) {
		this.accessTokenRepository = accessTokenRepository;
	}

	public boolean accessTokenIsValid(String token, List<TokenValidator> validators) {
		if (accessTokenRepository.contains(token)) {
			AccessToken accessToken = accessTokenRepository.get(token);
			for (TokenValidator validator : validators) {
				if (validator.validate(accessToken) == false) {
					System.out.println("Token invalid:" + validator.errorMessage());
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
}
