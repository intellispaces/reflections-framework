package intellispaces.framework.core.system;

import java.util.List;

/**
 * Module projection provider.
 */
public interface ProjectionProvider {

  <T> T getProjection(String name, Class<T> targetClass);

  <T> List<T> getProjections(Class<T> targetClass);
}
