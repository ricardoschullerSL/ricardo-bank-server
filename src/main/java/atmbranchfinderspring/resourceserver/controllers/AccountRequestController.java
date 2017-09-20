package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.annotations.AccessTokenAuthenticated;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManagerImpl;
import atmbranchfinderspring.resourceserver.models.AccountRequest;
import atmbranchfinderspring.resourceserver.models.AccountRequestResponse;
import atmbranchfinderspring.resourceserver.repos.AccountRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;


@RestController
public class AccountRequestController {

    private AccountRequestRepository accountRequestRepository;
    private AuthenticationManagerImpl authenticationManagerImpl;
    private ObjectMapper mapper;

    @Autowired
    public AccountRequestController(AccountRequestRepository accountRequestRepository, AuthenticationManagerImpl authenticationManagerImpl) {
        this.accountRequestRepository = accountRequestRepository;
        this.authenticationManagerImpl = authenticationManagerImpl;
        this.mapper = new ObjectMapper();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/account-requests")
    @AccessTokenAuthenticated
    public void postAccountRequest(HttpServletRequest request, HttpServletResponse response, @RequestBody AccountRequest accountRequest) throws IOException {
        AccountRequestResponse accountRequestResponse = accountRequestRepository.createAccountRequestResponse(accountRequest);
	    response.setHeader("Content-type", "application/json");
        mapper.writer().writeValue(response.getWriter(), accountRequestResponse);
        System.out.println(accountRequestRepository.getAllIds().size());
    }

    @RequestMapping(method = RequestMethod.GET, value= "/account-requests")
    @AccessTokenAuthenticated
    public void getAllAccountRequests(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Collection<String> responseObjects = accountRequestRepository.getAllIds();
        response.setHeader("Content-type", "application/json");
        mapper.writer().writeValue(response.getWriter(), responseObjects);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/account-requests/{accountRequestId}", produces = "application/json")
    @AccessTokenAuthenticated
    public void getAccountRequest(HttpServletRequest request, HttpServletResponse response, @PathVariable("accountRequestId") String accountRequestId) throws IOException {
        response.setHeader("Content-type", "application/json");
        System.out.println(accountRequestId);
        AccountRequestResponse requestResponse = accountRequestRepository.get(accountRequestId);
    	mapper.writer().writeValue(response.getWriter(), requestResponse);
    }


}
