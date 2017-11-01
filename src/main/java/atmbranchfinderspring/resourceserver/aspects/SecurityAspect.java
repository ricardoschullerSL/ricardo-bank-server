package atmbranchfinderspring.resourceserver.aspects;


import atmbranchfinderspring.resourceserver.annotations.AccessTokenAuthenticated;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import atmbranchfinderspring.resourceserver.validation.accountrequests.Permission;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * The Security Aspect class implements methods that check incoming requests, based on the annotation used on the
 * (Rest)Controller method.
 */

@Aspect
public class SecurityAspect {


	private AuthenticationManager authenticationManager;

	public SecurityAspect(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

    @Around("@annotation(atmbranchfinderspring.resourceserver.annotations.TPPBasicAuthenticated)")
    public void doTPPBasicAuthentication(ProceedingJoinPoint joinPoint) throws IOException {
		Object[] args = joinPoint.getArgs();
		HttpServletRequest request = (HttpServletRequest) args[0];
		HttpServletResponse response = (HttpServletResponse) args[1];
	    String[] values;
	    String authorization = request.getHeader("Authorization");
	    System.out.println("SecurityAspect aspect started.");
	    try {
		    if (authorization != null && authorization.startsWith("Basic")) {
			    String base64Credentials = authorization.substring("Basic".length()).trim();
			    String credentials = new String(Base64.getDecoder().decode(base64Credentials));
			    values = credentials.split(":", 2);
			    System.out.println("Authenticating " + values[0]);
			    if (authenticationManager.areClientCredentialsValid(values[0], values[1])) {
				    joinPoint.proceed();
			    } else {
				    response.sendError(403, "Incorrect Client Credentials.");
			    }
		    } else {
			    response.sendError(400, "No Authorization headers.");
		    }
	    } catch (Throwable e) {
		    e.printStackTrace();
		    response.sendError(500);
	    }
    }

	@Around("@annotation(atmbranchfinderspring.resourceserver.annotations.AdminBasicAuthenticated)")
	public void doAdminBasicAuthentication(ProceedingJoinPoint joinPoint) throws IOException {
		String[] values;
		Object[] args = joinPoint.getArgs();
		HttpServletRequest request = (HttpServletRequest) args[0];
		HttpServletResponse response = (HttpServletResponse) args[1];
		String authorization = request.getHeader("Authorization");
		System.out.println("SecurityAspect aspect started.");
		try {
			if (authorization != null && authorization.startsWith("Basic")) {
				String base64Credentials = authorization.substring("Basic".length()).trim();
				String credentials = new String(Base64.getDecoder().decode(base64Credentials));
				values = credentials.split(":", 2);
				System.out.println("Authenticating " + values[0]);
				if (authenticationManager.areAdminCredentialsValid(values[0], values[1])) {
					joinPoint.proceed();
				} else {
					response.sendError(403, "Incorrect Client Credentials.");
				}
			} else {
				response.sendError(400, "No Authorization headers.");
			}
		} catch (Throwable e) {
			System.out.println(e);
			response.sendError(500);
		}
	}

	@Around("@annotation(atmbranchfinderspring.resourceserver.annotations.RequestTokenAuthenticated)")
	public void RequestTokenAuthentication(ProceedingJoinPoint joinPoint) throws IOException {
		Object[] args = joinPoint.getArgs();
		HttpServletRequest request = (HttpServletRequest) args[0];
		HttpServletResponse response = (HttpServletResponse) args[1];
		String authorization = request.getHeader("Authorization");
		try {
			if (authorization != null && authorization.startsWith("Bearer")) {
				String token = authorization.substring("Bearer".length()).trim();
				System.out.println("Checking access token");
				if (authenticationManager.isRequestTokenValid(token)) {
					joinPoint.proceed();
				} else {
					response.sendError(403, "Access token not valid.");
				}
			} else {
				response.sendError(400, "No Authorization headers.");
			}
		} catch (Throwable e) {
			System.out.println(e);
			response.sendError(500);
		}
	}

	@Around("@annotation(atmbranchfinderspring.resourceserver.annotations.AccessTokenAuthenticated)")
	public void AccessTokenAuthentication(ProceedingJoinPoint joinPoint) throws IOException {
		Object[] args = joinPoint.getArgs();
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		AccessTokenAuthenticated accessTokenAuthenticated = signature.getMethod().getAnnotation(AccessTokenAuthenticated.class);
		Set<Permission> requiredPermission = new HashSet<Permission>(Arrays.asList(accessTokenAuthenticated.requiredPermission()));
		HttpServletRequest request = (HttpServletRequest) args[0];
		HttpServletResponse response = (HttpServletResponse) args[1];
		String authorization = request.getHeader("Authorization");
		try {
			if (authorization != null && authorization.startsWith("Bearer")) {
				String token = authorization.substring("Bearer".length()).trim();
				System.out.println("Checking access token");
				if (authenticationManager.isAccessTokenValid(token, requiredPermission)) {
					joinPoint.proceed();
				} else {
					response.sendError(403, "Access token not valid.");
				}
			} else {
				response.sendError(400, "No Authorization headers.");
			}
		} catch (Throwable e) {
			System.out.println(e);
			response.sendError(500);
		}
	}

}
