package atmbranchfinderspring.resourceserver.validation.accesstokens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * AccessTokenValidator follows the Command Pattern. It takes in list of {@link TokenValidator} which validate the token
 * in their own way. If any of these return false, it returns false to indicate the {@link AccessToken} is not valid.
 */

@Component
public class AccessTokenValidatorImpl implements AccessTokenValidator {

	@Autowired
	public AccessTokenValidatorImpl() {}

	public boolean accessTokenIsValid(AccessToken token, List<TokenValidator> validators) {

		if (validators.size() == 0) throw new IllegalArgumentException("List of validators can't be empty.");

		for (TokenValidator validator : validators) {
			if (validator.validate(token) == false) {
				System.out.println("Token invalid:" + validator.errorMessage());
				return false;
			}
		}
		return true;
	}
}
