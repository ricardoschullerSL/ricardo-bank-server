package atmbranchfinderspring.resourceserver.repos;

import java.util.Collection;
/**
 * @author Ricardo Schuller
 * @version 0.1.0
 * @since 0.1.0
 */
public interface Repository<T> {
    public void add(T entity);
    public Collection<T> getAll();
    public void delete(T entity);
    public void delete(String id);

}
