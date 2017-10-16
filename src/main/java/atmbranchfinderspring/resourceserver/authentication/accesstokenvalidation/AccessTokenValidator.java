package atmbranchfinderspring.resourceserver.authentication.accesstokenvalidation;

import atmbranchfinderspring.resourceserver.authentication.accesstokenvalidation.TokenValidator;

import java.util.List;

public interface AccessTokenValidator {

	boolean accessTokenIsValid(String token, List<TokenValidator> validators);
}
