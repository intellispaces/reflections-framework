package tech.intellispaces.reflectionsj.system;

import java.util.List;

/**
 * The module projection provider.
 */
public interface ProjectionProvider {

  <T> T getProjection(String name, Class<T> targetObjectHandleClass);

  <T> List<T> getProjections(Class<T> targetObjectHandleClass);
}
