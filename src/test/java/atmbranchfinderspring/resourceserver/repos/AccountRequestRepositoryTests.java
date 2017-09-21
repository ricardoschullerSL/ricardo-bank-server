package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.models.AccountRequest;
import atmbranchfinderspring.resourceserver.models.AccountRequestResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;



public class AccountRequestRepositoryTests {

	private AccountRequestRepository accountRequestRepository;
	private AccountRequestResponse response1;
	private AccountRequestResponse response2;

	@BeforeEach
	void setup() {
		accountRequestRepository = new AccountRequestRepository();
		AccountRequest accountRequest1 = new AccountRequest(null, new ArrayList<>(Arrays.asList("ReadPermission1", "ReadPermission2")), LocalDateTime.now(), LocalDateTime.now().plusSeconds(100L));
		AccountRequest accountRequest2 = new AccountRequest(null, new ArrayList<>(Arrays.asList("ReadPermission4", "ReadPermission5")), LocalDateTime.now(), LocalDateTime.now().plusSeconds(100L));
		response1 = accountRequestRepository.createAccountRequestResponse(accountRequest1);
		response2 = accountRequestRepository.createAccountRequestResponse(accountRequest2);
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
		AccountRequest accountRequest = new AccountRequest(null,new ArrayList<>(Arrays.asList("ReadPermission1", "ReadPermission2")), LocalDateTime.now(), LocalDateTime.now().plusSeconds(10L));
		accountRequestRepository.createAccountRequestResponse(accountRequest);
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
