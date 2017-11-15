package atmbranchfinderspring.resourceserver.validation;

import atmbranchfinderspring.resourceserver.validation.accesstokens.AccessToken;
import atmbranchfinderspring.resourceserver.validation.accesstokens.AccountRequestAuthorizationValidator;
import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequest;
import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountRequestAuthorizationValidatorTests {

	private AccountRequestAuthorizationValidator accountRequestAuthorizationValidator;


	@Test
	void validateCorrectToken() {
		int accountId = 1001;
		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setAccountId(accountId);
		accountRequest.setStatus(AccountRequest.AccountRequestStatus.AUTHORIZED);
		accountRequest.setAccountRequestId("testId");

		AccessToken token = new AccessToken("testClient", AccessToken.TokenType.ACCESS, "testToken", 100L, "testId");
		AccountRequestAuthorizationValidator validator = new AccountRequestAuthorizationValidator(accountRequest,accountId);
		assertThat(validator.validate(token)).isEqualTo(true);

	}

	@Test
	void validateTokenWithIncorrectAccountRequestId() {
		int accountId = 1001;
		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setAccountId(accountId);
		accountRequest.setStatus(AccountRequest.AccountRequestStatus.AUTHORIZED);
		accountRequest.setAccountRequestId("testId");

		AccessToken token = new AccessToken("testClient", AccessToken.TokenType.ACCESS, "testToken", 100L, "wrongId");
		AccountRequestAuthorizationValidator validator = new AccountRequestAuthorizationValidator(accountRequest, accountId);
		assertThat(validator.validate(token)).isEqualTo(false);
	}

	@Test
	void validateTokenWithIncorrectAccountId() {
		int accountId = 1001;
		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setAccountId(2002);
		accountRequest.setStatus(AccountRequest.AccountRequestStatus.AUTHORIZED);
		accountRequest.setAccountRequestId("testId");

		AccessToken token = new AccessToken("testClient", AccessToken.TokenType.ACCESS, "testToken", 100L, "testId");
		AccountRequestAuthorizationValidator validator = new AccountRequestAuthorizationValidator(accountRequest,accountId);
		assertThat(validator.validate(token)).isEqualTo(false);

	}

	@Test
	void validateTokenWithIncorrectStatus() {
		int accountId = 1001;
		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setAccountId(accountId);
		accountRequest.setStatus(AccountRequest.AccountRequestStatus.AWAITINGAUTHORIZATION);
		accountRequest.setAccountRequestId("testId");

		AccessToken token = new AccessToken("testClient", AccessToken.TokenType.ACCESS, "testToken", 100L, "testId");
		AccountRequestAuthorizationValidator validator = new AccountRequestAuthorizationValidator(accountRequest,accountId);
		assertThat(validator.validate(token)).isEqualTo(false);

	}
}
