package atmbranchfinderspring.resourceserver.validation.accesstokens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccessTokenValidatorImpl implements AccessTokenValidator {

	@Autowired
	public AccessTokenValidatorImpl() {}

	public boolean accessTokenIsValid(AccessToken token, List<TokenValidator> validators) {
		for (TokenValidator validator : validators) {
			if (validator.validate(token) == false) {
				System.out.println("Token invalid:" + validator.errorMessage());
				return false;
			}
		}
		return true;
	}
}
