package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.authentication.AccountRequestValidator;
import atmbranchfinderspring.resourceserver.models.AccountRequest;
import atmbranchfinderspring.resourceserver.models.AccountRequestResponse;
import atmbranchfinderspring.resourceserver.models.Permission;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



public class AccountRequestRepositoryTests {

	private AccountRequestRepository accountRequestRepository;
	private AccountRequestValidator accountRequestValidator = new AccountRequestValidator();
	private AccountRequestResponse response1;
	private AccountRequestResponse response2;

	@BeforeEach
	void setup() {
		accountRequestRepository = new AccountRequestRepository();
		AccountRequest accountRequest1 = new AccountRequest(null, new ArrayList<>(Arrays.asList("ReadAccountsBasic", "ReadAccountsDetail")), LocalDateTime.now(), LocalDateTime.now().plusSeconds(100L));
		AccountRequest accountRequest2 = new AccountRequest(null, new ArrayList<>(Arrays.asList("ReadBalances", "ReadTransactionsBasic")), LocalDateTime.now(), LocalDateTime.now().plusSeconds(100L));
		List<Permission> permissions1 = accountRequestValidator.convertPermissions(accountRequest1.getPermissions());
		List<Permission> permissions2 = accountRequestValidator.convertPermissions(accountRequest2.getPermissions());
		response1 = accountRequestRepository.createAccountRequestResponse(accountRequest1, permissions1);
		response2 = accountRequestRepository.createAccountRequestResponse(accountRequest2, permissions2);
	}

	@AfterEach
	void cleanup() {
		accountRequestRepository = null;
	}

	@Test
	void getGetsToken() {
		AccountRequestResponse accountRequest = accountRequestRepository.get(response1.getAccountRequestId());
		assertThat(accountRequest.getAccountRequestId()).isEqualTo(response1.getAccountRequestId());
	}

	@Test
	void getsReturnNullIfTokenDoesNotExist() {
		AccountRequestResponse accountRequestResponse = accountRequestRepository.get("aaa");
		assertThat(accountRequestResponse).isEqualTo(null);
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
		AccountRequest accountRequest = new AccountRequest(null,new ArrayList<>(Arrays.asList("ReadAccountsBasic", "ReadAccountsDetail")), LocalDateTime.now(), LocalDateTime.now().plusSeconds(10L));
		List<Permission> permissions = accountRequestValidator.convertPermissions(accountRequest.getPermissions());
		accountRequestRepository.createAccountRequestResponse(accountRequest, permissions);
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
