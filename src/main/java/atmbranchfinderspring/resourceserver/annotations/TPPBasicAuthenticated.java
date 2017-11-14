package atmbranchfinderspring.resourceserver.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * TPPBasicAuthenticated is an annotation that can be used on (Rest)Controller methods that need to be authenticated
 * by Basic Authentication just for Third Party Providers. The methods that use this annotation are required to have an
 * HttpServletRequest and HttpServletResponse parameter as their first and second arguments respectively.
 */

@Target(value= {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TPPBasicAuthenticated {
}
