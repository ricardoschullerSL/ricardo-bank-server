package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequest;
import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequestValidator;
import atmbranchfinderspring.resourceserver.validation.accountrequests.IncomingAccountRequest;
import atmbranchfinderspring.resourceserver.validation.accountrequests.Permission;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;



public class IncomingAccountRequestRepositoryTests {

	private AccountRequestRepository accountRequestRepository;
	private AccountRequestValidator accountRequestValidator = new AccountRequestValidator();
	private AccountRequest response1;
	private AccountRequest response2;

	@BeforeEach
	void setup() {
		accountRequestRepository = new AccountRequestRepository();
		IncomingAccountRequest incomingAccountRequest1 = new IncomingAccountRequest(null, new ArrayList<>(Arrays.asList("ReadAccountsBasic", "ReadAccountsDetail")), LocalDateTime.now(), LocalDateTime.now().plusSeconds(100L));
		IncomingAccountRequest incomingAccountRequest2 = new IncomingAccountRequest(null, new ArrayList<>(Arrays.asList("ReadBalances", "ReadTransactionsBasic")), LocalDateTime.now(), LocalDateTime.now().plusSeconds(100L));
		Set<Permission> permissions1 = accountRequestValidator.convertPermissions(incomingAccountRequest1.getPermissions());
		Set<Permission> permissions2 = accountRequestValidator.convertPermissions(incomingAccountRequest2.getPermissions());
		response1 = accountRequestRepository.createAccountRequestResponse(incomingAccountRequest1, permissions1, "testClient1");
		response2 = accountRequestRepository.createAccountRequestResponse(incomingAccountRequest2, permissions2, "testClient2");
	}

	@AfterEach
	void cleanup() {
		accountRequestRepository = null;
	}

	@Test
	void getGetsToken() {
		AccountRequest accountRequest = accountRequestRepository.get(response1.getAccountRequestId());
		assertThat(accountRequest.getAccountRequestId()).isEqualTo(response1.getAccountRequestId());
	}

	@Test
	void getsReturnNullIfTokenDoesNotExist() {
		AccountRequest accountRequest = accountRequestRepository.get("aaa");
		assertThat(accountRequest).isEqualTo(null);
	}

	@Test
	void containsReturnsBooleanTrue() {
		assertThat(accountRequestRepository.contains(response1.getAccountRequestId())).isEqualTo(true);
	}

	@Test
	void containsReturnsBooleanFalse() {
		assertThat(accountRequestRepository.contains("aaa")).isEqualTo(false);
	}

	@Test
	void addAddsToken() {
		int size = accountRequestRepository.getAllIds().size();
		IncomingAccountRequest incomingAccountRequest = new IncomingAccountRequest(null,new ArrayList<>(Arrays.asList("ReadAccountsBasic", "ReadAccountsDetail")), LocalDateTime.now(), LocalDateTime.now().plusSeconds(10L));
		Set<Permission> permissions = accountRequestValidator.convertPermissions(incomingAccountRequest.getPermissions());
		accountRequestRepository.createAccountRequestResponse(incomingAccountRequest, permissions, "testClient");
		assertThat(accountRequestRepository.getAllIds().size()).isEqualTo(size + 1);
	}

	@Test
	void deleteDeletesToken() {
		int size = accountRequestRepository.getAllIds().size();
		accountRequestRepository.delete(response1.getAccountRequestId());
		assertThat(accountRequestRepository.getAllIds().size()).isEqualTo(size - 1);
	}

	@Test
	void deleteDeletesTokenWithTokenParameter() {
		int size = accountRequestRepository.getAllIds().size();
		accountRequestRepository.delete(response1);
		assertThat(accountRequestRepository.getAllIds().size()).isEqualTo(size - 1);
	}
}
