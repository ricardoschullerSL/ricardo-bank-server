package atmbranchfinderspring.resourceserver.validation.accesstokens;

import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequest;

public class AccountRequestAuthorizationValidator implements TokenValidator {

	private AccountRequest accountRequest;
	private int accountId;

	public AccountRequestAuthorizationValidator(AccountRequest accountRequest, int accountId) {
		this.accountRequest = accountRequest;
		this.accountId = accountId;
	}

	@Override
	public boolean validate(AccessToken token) {

		if (token.getAccountRequestId().equals(accountRequest.getAccountRequestId())
				&& (accountRequest.getAccountId() == accountId)
				&& accountRequest.getStatus().equals(AccountRequest.AccountRequestStatus.AUTHORIZED)) {
			return true;
		}
		return false;
	}

	@Override
	public String errorMessage() {
		return "AccountRequest not valid.";
	}
}
