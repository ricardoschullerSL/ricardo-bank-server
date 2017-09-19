package atmbranchfinderspring.resourceserver.repos;

import java.util.Collection;

public interface Repository<T> {
    void add(T entity);
    T get(String id);
    Collection<String> getAllIds();
    Boolean contains(String id);
    void delete(T entity);
    void delete(String id);

}
