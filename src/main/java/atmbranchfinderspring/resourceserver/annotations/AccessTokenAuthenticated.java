package atmbranchfinderspring.resourceserver.annotations;

import atmbranchfinderspring.resourceserver.validation.accountrequests.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AccessTokenAuthenticated is an annotation that can be used on (Rest)Controller methods that need to be authenticated
 * using "Authorization-code" grant type access tokens. The methods that use this annotation are required to have an
 * HttpServletRequest and HttpServletResponse parameter as their first and second arguments respectively.
 */

@Target(value= {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface AccessTokenAuthenticated {
	Permission[] requiredPermission();
}
