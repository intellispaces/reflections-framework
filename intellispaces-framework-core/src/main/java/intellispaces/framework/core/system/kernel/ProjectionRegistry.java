package intellispaces.framework.core.system.kernel;

import intellispaces.framework.core.system.ModuleProjection;
import intellispaces.framework.core.system.ProjectionProvider;

import java.util.Collection;

/**
 * Projection register.
 */
public interface ProjectionRegistry extends ProjectionProvider {

  void load();

  Collection<ModuleProjection> allProjections();

  <T> void addContextProjection(String name, Class<T> type, T target);

  void removeContextProjection(String name);
}
