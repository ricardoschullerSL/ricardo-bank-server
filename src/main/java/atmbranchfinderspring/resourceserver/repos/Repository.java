package atmbranchfinderspring.resourceserver.repos;

import java.util.Collection;
/**
 * @author Ricardo Schuller
 * @version 0.1.0
 * @since 0.1.0
 */
public interface Repository<T> {
    void add(T entity);
    T get(String id);
    Collection<String> getAllIds();
    Boolean contains(String id);
    void delete(T entity);
    void delete(String id);

}
