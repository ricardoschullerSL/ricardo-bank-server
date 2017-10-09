package atmbranchfinderspring.resourceserver.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * RequestTokenAuthenticated is an annotation that can be used on (Rest)Controller methods that need to be authenticated
 * using "Client-Credentials" grant type access tokens. The methods that use this annotation are required to have an
 * HttpServletRequest and HttpServletResponse parameter as their first and second arguments respectively.
 */

@Component
@Target(value= {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RequestTokenAuthenticated {
}
