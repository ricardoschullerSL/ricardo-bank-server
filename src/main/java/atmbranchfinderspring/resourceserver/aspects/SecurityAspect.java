package atmbranchfinderspring.resourceserver.aspects;


import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;


@Aspect
public class SecurityAspect {


	private AuthenticationManager authenticationManager;

	@Autowired
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
			    if (authenticationManager.checkClientCredentials(values[0], values[1])) {
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

	@Around("@annotation(atmbranchfinderspring.resourceserver.annotations.AdminBasicAuthenticated)")
	public void doAdminBasicAuthentication(ProceedingJoinPoint joinPoint) throws IOException {
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
				if (authenticationManager.checkAdminCredentials(values[0], values[1])) {
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

	@Around("@annotation(atmbranchfinderspring.resourceserver.annotations.AccessTokenAuthenticated)")
	public void AccessTokenAuthentication(ProceedingJoinPoint joinPoint) throws IOException {
		Object[] args = joinPoint.getArgs();
		HttpServletRequest request = (HttpServletRequest) args[0];
		HttpServletResponse response = (HttpServletResponse) args[1];
		String authorization = request.getHeader("Authorization");
		try {
			if (authorization != null && authorization.startsWith("Bearer")) {
				String token = authorization.substring("Bearer".length()).trim();
				System.out.println("Checking access token");
				if (authenticationManager.isAccessTokenValid(token)) {
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
