package intellispaces.jaquarius.system.kernel;

import intellispaces.jaquarius.system.ModuleProjection;
import intellispaces.jaquarius.system.ProjectionProvider;

import java.util.Collection;

/**
 * Projection register.
 */
public interface ProjectionRegistry extends ProjectionProvider {

  void load();

  Collection<ModuleProjection> allProjections();

  <T> void addContextProjection(String name, Class<T> targetObjectHandleClass, T target);

  void removeContextProjection(String name);
}
