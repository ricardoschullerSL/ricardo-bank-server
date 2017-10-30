package atmbranchfinderspring.resourceserver.validation.accesstokens;

import java.util.List;

public interface AccessTokenValidator {

	boolean accessTokenIsValid(String token, List<TokenValidator> validators);
}
