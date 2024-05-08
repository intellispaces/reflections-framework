package tech.intellispacesframework.core.system;

import tech.intellispacesframework.core.object.ObjectFunctions;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the {@link ProjectionRegistry}.
 */
class ProjectionRegistryDefault implements ProjectionRegistry {
  private final Map<String, SystemProjection> projections = new HashMap<>();

  @Override
  @SuppressWarnings("unchecked")
  public <T> Optional<T> getProjection(String name, Class<T> targetClass) {
    SystemProjection p = projections.get(name);
    if (p == null) {
      return Optional.empty();
    }
    if (!ObjectFunctions.isCompatibleObjectType(targetClass, p.targetClass())) {
      return Optional.empty();
    }
    return (Optional<T>) Optional.ofNullable(p.target());
  }

  @Override
  public void addProjection(SystemProjection projection) {
    projections.put(projection.name(), projection);
  }

  @Override
  public Collection<SystemProjection> allProjections() {
    return Collections.unmodifiableCollection(projections.values());
  }
}
