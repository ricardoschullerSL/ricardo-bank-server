package atmbranchfinderspring.resourceserver.aspects;


import atmbranchfinderspring.resourceserver.annotations.AccessTokenAuthenticated;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import atmbranchfinderspring.resourceserver.validation.accesstokens.AccessToken;
import atmbranchfinderspring.resourceserver.validation.accountrequests.Permission;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

/**
 * The Security Aspect class implements methods that check incoming requests, based on the annotation used on the
 * (Rest)Controller method.
 */


@Aspect
@Component
public class SecurityAspect {


	private AuthenticationManager authenticationManager;
	public static final String SERVER_ERROR_MESSAGE = "Something went wrong";
	public static final String INVALID_AUTHORIZATION_HEADER_MESSAGE = "Invalid authorization header.";
	public static final String INVALID_ACCESS_TOKEN_MESSAGE = "Invalid access token.";
	public static final String INCORRECT_CREDENTIALS_MESSAGE = "Incorrect credentials.";

	public SecurityAspect(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

    @Around("@annotation(atmbranchfinderspring.resourceserver.annotations.TPPBasicAuthenticated)")
    public Object doTPPBasicAuthentication(ProceedingJoinPoint joinPoint) throws IOException {
		Object[] args = joinPoint.getArgs();
		HttpServletRequest request = (HttpServletRequest) args[0];
		HttpServletResponse response = (HttpServletResponse) args[1];
	    String[] values;
	    String authorization = request.getHeader("Authorization");
	    try {
		    if (authorization != null && authorization.startsWith("Basic")) {
			    String base64Credentials = authorization.substring("Basic".length()).trim();
			    String credentials = new String(Base64.getDecoder().decode(base64Credentials));
			    values = credentials.split(":", 2);
			    System.out.println("Authenticating " + values[0]);
			    if (authenticationManager.areClientCredentialsValid(values[0], values[1])) {
				    return joinPoint.proceed();
			    } else {
				    response.sendError(403, INCORRECT_CREDENTIALS_MESSAGE);
			    }
		    } else {
			    response.sendError(400, INVALID_AUTHORIZATION_HEADER_MESSAGE);
		    }
	    } catch (Throwable e) {
		    e.printStackTrace();
		    response.sendError(500, SERVER_ERROR_MESSAGE);
	    }
	    return null;
    }

	@Around("@annotation(atmbranchfinderspring.resourceserver.annotations.AdminBasicAuthenticated)")
	public Object doAdminBasicAuthentication(ProceedingJoinPoint joinPoint) throws IOException {
		String[] values;
		Object[] args = joinPoint.getArgs();
		HttpServletRequest request = (HttpServletRequest) args[0];
		HttpServletResponse response = (HttpServletResponse) args[1];
		String authorization = request.getHeader("Authorization");
		try {
			if (authorization != null && authorization.startsWith("Basic")) {
				String base64Credentials = authorization.substring("Basic".length()).trim();
				String credentials = new String(Base64.getDecoder().decode(base64Credentials));
				values = credentials.split(":", 2);
				System.out.println("Authenticating " + values[0]);
				if (authenticationManager.areAdminCredentialsValid(values[0], values[1])) {
					return joinPoint.proceed();
				} else {
					response.sendError(403, INCORRECT_CREDENTIALS_MESSAGE);
				}
			} else {
				response.sendError(400, INVALID_AUTHORIZATION_HEADER_MESSAGE);
			}
		} catch (Throwable e) {
			System.out.println(e);
			response.sendError(500, SERVER_ERROR_MESSAGE);
		}
		return null;
	}

	@Around("@annotation(atmbranchfinderspring.resourceserver.annotations.AccessTokenAuthenticated)")
	public Object AccessTokenAuthentication(ProceedingJoinPoint joinPoint) throws IOException {
		Object[] args = joinPoint.getArgs();
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		AccessTokenAuthenticated accessTokenAuthenticated = signature.getMethod().getAnnotation(AccessTokenAuthenticated.class);
		Set<Permission> requiredPermission = new HashSet<Permission>(Arrays.asList(accessTokenAuthenticated.requiredPermission()));
		AccessToken.TokenType requiredTokenType = accessTokenAuthenticated.tokenType();
		HttpServletRequest request = (HttpServletRequest) args[0];
		HttpServletResponse response = (HttpServletResponse) args[1];
		String authorization = request.getHeader("Authorization");
		try {
			if (authorization != null && authorization.startsWith("Bearer")) {
				String token = authorization.substring("Bearer".length()).trim();
				System.out.println("Checking access token");
				if (authenticationManager.isAccessTokenValid(request, token, requiredPermission, requiredTokenType)) {
					return joinPoint.proceed();
				} else {
					response.sendError(403, INVALID_ACCESS_TOKEN_MESSAGE);
				}
			} else {
				response.sendError(400, INVALID_AUTHORIZATION_HEADER_MESSAGE);
			}
		} catch (Throwable e) {
			System.out.println(e);
			response.sendError(500, SERVER_ERROR_MESSAGE);
		}
		return null;
	}

//	@Deprecated
//	@Around("@annotation(atmbranchfinderspring.resourceserver.annotations.RequestTokenAuthenticated)")
//	public Object RequestTokenAuthentication(ProceedingJoinPoint joinPoint) throws IOException {
//		Object[] args = joinPoint.getArgs();
//		HttpServletRequest request = (HttpServletRequest) args[0];
//		HttpServletResponse response = (HttpServletResponse) args[1];
//		String authorization = request.getHeader("Authorization");
//		try {
//			if (authorization != null && authorization.startsWith("Bearer")) {
//				String token = authorization.substring("Bearer".length()).trim();
//				System.out.println("Checking access token");
//				if (authenticationManager.isRequestTokenValid(token)) {
//					return joinPoint.proceed();
//				} else {
//					response.sendError(403, INVALID_ACCESS_TOKEN_MESSAGE);
//				}
//			} else {
//				response.sendError(400, INVALID_AUTHORIZATION_HEADER_MESSAGE);
//			}
//		} catch (Throwable throwable) {
//			throwable.printStackTrace();
//			response.sendError(500, SERVER_ERROR_MESSAGE);
//		}
//		return null;
//	}
}
