package atmbranchfinderspring.resourceserver.aspects;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import javax.servlet.http.HttpServletRequest;

@Aspect
public class GeneralEndPoint {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {
    }

    @Pointcut("execution(* *.*(..))")
    protected void allMethod() {

    }

    @Before("restController() && allMethod()")
    public void doAccessTokenCheck(JoinPoint joinPoint, HttpServletRequest request) {
        System.out.println("Something happened");
        System.out.println(joinPoint.getSignature().getName());
        System.out.println(request.getHeaders("Content-Type"));
    }

}
