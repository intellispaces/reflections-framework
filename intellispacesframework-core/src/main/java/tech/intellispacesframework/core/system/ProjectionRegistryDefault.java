package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.classes.ClassFunctions;

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

    Class<?> actualTargetClass = ClassFunctions.getObjectClass(targetClass);
    Class<?> actualProjectionTargetClass = ClassFunctions.getObjectClass(p.targetClass());
    if (actualProjectionTargetClass == actualTargetClass || actualTargetClass.isAssignableFrom(actualProjectionTargetClass)) {
      return (Optional<T>) Optional.ofNullable(p.target());
    }
    return Optional.empty();
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
