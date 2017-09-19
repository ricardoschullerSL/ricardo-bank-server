package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.AuthenticationManagerImpl;
import atmbranchfinderspring.resourceserver.models.AccountRequest;
import atmbranchfinderspring.resourceserver.models.AccountRequestResponse;
import atmbranchfinderspring.resourceserver.models.ResponseObject;
import atmbranchfinderspring.resourceserver.repos.AccountRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;


@RestController
public class AccountRequestController {

    private AccountRequestRepository accountRequestRepository;
    private AuthenticationManagerImpl authenticationManagerImpl;

    @Autowired
    public AccountRequestController(AccountRequestRepository accountRequestRepository, AuthenticationManagerImpl authenticationManagerImpl) {
        this.accountRequestRepository = accountRequestRepository;
        this.authenticationManagerImpl = authenticationManagerImpl;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/account-requests")
    public ResponseObject postAccountRequest(@RequestBody AccountRequest accountRequest, HttpServletRequest request, HttpServletResponse response) {
        String token;
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer")) {
            token = authorization.substring("Bearer".length()).trim();
            System.out.println(token);
            if (authenticationManagerImpl.isAccessTokenValid(token)) {
                AccountRequestResponse accountRequestResponse = accountRequestRepository.createAccountRequestResponse(accountRequest);
                return accountRequestResponse;
            }
        }
        try {
            response.sendError(403);

        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value= "/account-requests")
    public Collection<String> getAllAccountRequests() {
        Collection<String> responseObjects = accountRequestRepository.getAllIds();
        System.out.println(responseObjects.size());
        return responseObjects;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/account-requests/{accountRequestId}", produces = "application/json")
    public ResponseObject getAccountRequest(@Param("accountRequestId") String accountRequestId) {
        return accountRequestRepository.get(accountRequestId);
    }


}
