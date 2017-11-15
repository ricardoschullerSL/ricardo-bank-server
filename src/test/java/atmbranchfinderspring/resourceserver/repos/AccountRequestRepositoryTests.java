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
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;



public class AccountRequestRepositoryTests {

	private AccountRequestRepository accountRequestRepository;
	private AccountRequestValidator accountRequestValidator = new AccountRequestValidator();
	private Set<Permission> permissions1 = new HashSet<Permission>(){{add(Permission.ReadAccountsBasic); add(Permission.ReadAccountsDetail);}};
	private Set<Permission> permissions2 = new HashSet<Permission>(){{add(Permission.ReadBalances); add(Permission.ReadTransactionsBasic);}};
	private AccountRequest accountRequest1 = new AccountRequest("1001", "testClient1",LocalDateTime.now(), LocalDateTime.now().plusSeconds(100L), permissions1, LocalDateTime.now(), LocalDateTime.now().plusSeconds(100L), AccountRequest.AccountRequestStatus.AWAITINGAUTHORIZATION);
	private AccountRequest accountRequest2 = new AccountRequest("2002", "testClient2", LocalDateTime.now(), LocalDateTime.now().plusSeconds(100L),permissions2, LocalDateTime.now(), LocalDateTime.now().plusSeconds(100L), AccountRequest.AccountRequestStatus.AUTHORIZED);



	@BeforeEach
	void setup() {
		accountRequestRepository = new AccountRequestRepository();
		accountRequestRepository.add(accountRequest1);
		accountRequestRepository.add(accountRequest2);
	}

	@AfterEach
	void cleanup() {
		accountRequestRepository = null;
	}

	@Test
	void getGetsToken() {
		AccountRequest accountRequest = accountRequestRepository.get("1001");
		assertThat(accountRequest.getClientId()).isEqualTo("testClient1");
	}

	@Test
	void getsReturnNullIfTokenDoesNotExist() {
		AccountRequest accountRequest = accountRequestRepository.get("wrongId");
		assertThat(accountRequest).isEqualTo(null);
	}

	@Test
	void containsReturnsBooleanTrue() {
		assertThat(accountRequestRepository.contains(accountRequest1.getAccountRequestId())).isEqualTo(true);
	}

	@Test
	void containsReturnsBooleanFalse() {
		assertThat(accountRequestRepository.contains("wrongId")).isEqualTo(false);
	}

	@Test
	void addAddsToken() {
		int originalSize = accountRequestRepository.getAllIds().size();
		AccountRequest accountRequest = new AccountRequest("3003", "testClient3", LocalDateTime.now(),
				LocalDateTime.now().plusSeconds(100L), new HashSet<Permission>(){{add(Permission.ReadTransactionsBasic);}},
				LocalDateTime.now(), LocalDateTime.now().plusSeconds(100L), AccountRequest.AccountRequestStatus.AUTHORIZED);
		accountRequestRepository.add(accountRequest);
		assertThat(accountRequestRepository.getAllIds().size()).isEqualTo(originalSize + 1);
	}

	@Test
	void deleteDeletesToken() {
		int size = accountRequestRepository.getAllIds().size();
		accountRequestRepository.delete(accountRequest1.getAccountRequestId());
		assertThat(accountRequestRepository.getAllIds().size()).isEqualTo(size - 1);
	}

	@Test
	void deleteDeletesTokenWithTokenParameter() {
		int size = accountRequestRepository.getAllIds().size();
		accountRequestRepository.delete(accountRequest1);
		assertThat(accountRequestRepository.getAllIds().size()).isEqualTo(size - 1);
	}

	@Test
	void createAccountRequestFromIncomingAccountRequest() {
		IncomingAccountRequest incomingAccountRequest = new IncomingAccountRequest("testId",
				new ArrayList<String>(){{add("ReadAccountDetail");}}, LocalDateTime.now(),
				LocalDateTime.now().plusSeconds(100L));
		Set<Permission> permissions = new HashSet<Permission>(){{add(Permission.ReadAccountsBasic);}};
		AccountRequest accountRequest = accountRequestRepository.createAccountRequestResponse(incomingAccountRequest,
				permissions, "testClient", 1001);
		assertThat(accountRequest.getPermissions()).isEqualTo(permissions);
		assertThat(accountRequest.getClientId()).isEqualTo("testClient");
	}
}
