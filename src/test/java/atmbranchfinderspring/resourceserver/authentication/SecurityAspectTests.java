package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.aspects.SecurityAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class SecurityAspectTests {

	private AuthenticationManager authenticationManager;
	private AspectJProxyFactory aspectJProxyFactory;
	private SecurityAspect securityAspect;

	@BeforeEach
	void setup() {
		authenticationManager = mock(AuthenticationManager.class);
		securityAspect = new SecurityAspect(authenticationManager);
	}

	@Test
	void doCorrectTPPBasicAuthenticationTest() throws Throwable {
		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(joinPoint.getArgs()).thenReturn(new Object[]{request, response});
		when(request.getHeader("Authorization")).thenReturn("Basic dGVzdGNsaWVudDp0ZXN0cGFzc3dvcmQ=");
		when(authenticationManager.areClientCredentialsValid("testclient", "testpassword")).thenReturn(true);
		securityAspect.doTPPBasicAuthentication(joinPoint);
		verify(joinPoint, times(1)).proceed();
	}

	@Test
	void doIncorrectTPPBasicAuthenticationTest() throws Throwable {
		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(joinPoint.getArgs()).thenReturn(new Object[]{request, response});
		when(request.getHeader("Authorization")).thenReturn("Basic dGVzdGNsaWVudDp0ZXN0cGFzc3dvcmQ=");
		when(authenticationManager.areClientCredentialsValid("testclient", "testpassword")).thenReturn(false);
		securityAspect.doTPPBasicAuthentication(joinPoint);
		verify(joinPoint, times(0)).proceed();
		verify(response,times(1)).sendError(403, securityAspect.INCORRECT_CREDENTIALS_MESSAGE );
	}

	@Test
	void doIncorrectHeaderTPPBasicAuthenticationTest() throws Throwable {
		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(joinPoint.getArgs()).thenReturn(new Object[]{request, response});
		when(request.getHeader("Authorization")).thenReturn(null);
		when(authenticationManager.areClientCredentialsValid("testclient", "testpassword")).thenReturn(false);
		securityAspect.doTPPBasicAuthentication(joinPoint);
		verify(joinPoint, times(0)).proceed();
		verify(response,times(1)).sendError(400, securityAspect.INVALID_AUTHORIZATION_HEADER_MESSAGE );
	}

	@Test
	void doAdminBasicAuthenticationTest() throws Throwable {
		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(joinPoint.getArgs()).thenReturn(new Object[]{request, response});
		when(request.getHeader("Authorization")).thenReturn("Basic dGVzdGFkbWluOnRlc3RwYXNzd29yZA==");
		when(authenticationManager.areAdminCredentialsValid("testadmin", "testpassword")).thenReturn(true);
		securityAspect.doAdminBasicAuthentication(joinPoint);
		verify(joinPoint, times(1)).proceed();
	}

	@Test
	void doIncorrectAdminBasicAuthenticationTest() throws Throwable {
		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(joinPoint.getArgs()).thenReturn(new Object[]{request, response});
		when(request.getHeader("Authorization")).thenReturn("Basic dGVzdGFkbWluOnRlc3RwYXNzd29yZA==");
		when(authenticationManager.areAdminCredentialsValid("testadmin", "testpassword")).thenReturn(false);
		securityAspect.doAdminBasicAuthentication(joinPoint);
		verify(joinPoint, times(0)).proceed();
		verify(response,times(1)).sendError(403, securityAspect.INCORRECT_CREDENTIALS_MESSAGE );
	}

	@Test
	void doIncorrectHeaderAdminBasicAuthenticationTest() throws Throwable {
		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(joinPoint.getArgs()).thenReturn(new Object[]{request, response});
		when(request.getHeader("Authorization")).thenReturn(null);
		when(authenticationManager.areAdminCredentialsValid("testadmin", "testpassword")).thenReturn(false);
		securityAspect.doAdminBasicAuthentication(joinPoint);
		verify(joinPoint, times(0)).proceed();
		verify(response,times(1)).sendError(400, securityAspect.INVALID_AUTHORIZATION_HEADER_MESSAGE );
	}

	@Test
	void doRequestTokenAuthenticationTest() throws Throwable {
		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(joinPoint.getArgs()).thenReturn(new Object[]{request, response});
		when(request.getHeader("Authorization")).thenReturn("Bearer something");
		when(authenticationManager.isRequestTokenValid("something")).thenReturn(true);
		securityAspect.RequestTokenAuthentication(joinPoint);
		verify(joinPoint, times(1)).proceed();
	}

	@Test
	void doIncorrectRequestTokenAuthenticationTest() throws Throwable {
		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(joinPoint.getArgs()).thenReturn(new Object[]{request, response});
		when(request.getHeader("Authorization")).thenReturn("Bearer something");
		when(authenticationManager.isRequestTokenValid("something")).thenReturn(false);
		securityAspect.RequestTokenAuthentication(joinPoint);
		verify(joinPoint, times(0)).proceed();
		verify(response,times(1)).sendError(403, securityAspect.INVALID_ACCESS_TOKEN_MESSAGE );
	}

	@Test
	void doIncorrectHeaderRequestTokenAuthenticationTest() throws Throwable {
		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(joinPoint.getArgs()).thenReturn(new Object[]{request, response});
		when(request.getHeader("Authorization")).thenReturn(null);
		when(authenticationManager.isRequestTokenValid("something")).thenReturn(false);
		securityAspect.RequestTokenAuthentication(joinPoint);
		verify(joinPoint, times(0)).proceed();
		verify(response,times(1)).sendError(400, securityAspect.INVALID_AUTHORIZATION_HEADER_MESSAGE);
	}

	// TODO Find a way to test AccessTokenAuthentication()
//	@Test
//	void doAccessTokenAuthenticationTest() throws Throwable {
//		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
//		HttpServletRequest request = mock(HttpServletRequest.class);
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		when(joinPoint.getArgs()).thenReturn(new Object[]{request, response});
//		when(joinPoint.getSignature()).
//		when(request.getHeader("Authorization")).thenReturn("Bearer something");
//		when(authenticationManager.isRequestTokenValid("something")).thenReturn(true);
//		securityAspect.AccessTokenAuthentication(joinPoint);
//		verify(joinPoint, times(1)).proceed();
//	}
}
