package atmbranchfinderspring.resourceserver.validation.accesstokens;

import java.util.List;

public interface AccessTokenValidator {

	boolean accessTokenIsValid(AccessToken token, List<TokenValidator> validators);
}
