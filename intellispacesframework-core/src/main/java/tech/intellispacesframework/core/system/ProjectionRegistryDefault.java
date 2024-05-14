package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.exception.ExceptionFunctions;
import tech.intellispacesframework.core.exception.ConfigurationException;
import tech.intellispacesframework.core.exception.CyclicDependencyException;
import tech.intellispacesframework.core.object.ObjectFunctions;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Default implementation of the {@link ProjectionRegistry}.
 */
class ProjectionRegistryDefault implements ProjectionRegistry {
  private final Map<String, ProjectionProvider> projectionProviders;
  private final Map<String, SystemProjection> projections = new HashMap<>();

  ProjectionRegistryDefault(List<ProjectionProvider> projectionProviders) {
    this.projectionProviders = projectionProviders.stream()
        .collect(Collectors.toMap(ProjectionProvider::name, Function.identity()));
  }

  @Override
  public <T> T projection(String name, Class<T> targetClass) {
    return getProjection(name, targetClass, new LinkedHashSet<>());
  }

  @Override
  public Collection<SystemProjection> loadedProjections() {
    return Collections.unmodifiableCollection(projections.values());
  }

  void load() {
    projectionProviders.values().stream()
        .filter(provider -> !provider.isLazy())
        .forEach(provider -> defineProjection(provider, new LinkedHashSet<>()));
  }

  @SuppressWarnings("unchecked")
  private  <T> T getProjection(String name, Class<T> targetClass, Set<String> dependencyPath) {
    SystemProjection p = getProjection(name, dependencyPath);
    if (p == null) {
      return null;
    }
    if (!ObjectFunctions.isCompatibleObjectType(targetClass, p.targetClass())) {
      return null;
    }
    return (T) p.target();
  }

  private SystemProjection getProjection(String name, Set<String> dependencyPath) {
    SystemProjection projection = projections.get(name);
    if (projection != null) {
      return projection;
    }

    ProjectionProvider provider = projectionProviders.get(name);
    if (provider == null) {
      return null;
    }
    return defineProjection(provider, dependencyPath);
  }

  private SystemProjection defineProjection(ProjectionProvider provider, Set<String> dependencyPath) {
    dependencyPath.add(provider.name());
    if (ProjectionProviderTypes.UnitMethod == provider.providerType()) {
      return defineProjection((UnitProjectionProvider) provider, dependencyPath);
    } else {
      throw new UnsupportedOperationException("Unsupported projection provider type - " + provider.type());
    }
  }

  private SystemProjection defineProjection(UnitProjectionProvider provider, Set<String> dependencyPath) {
    final Object target;
    try {
      Method providerMethod = provider.providerMethod();
      Object[] providerArguments = getProviderArguments(provider, dependencyPath);
      target = providerMethod.invoke(provider.unit().instance(), providerArguments);
    } catch (Exception e) {
      throw ExceptionFunctions.coverException(e);
    }
    SystemProjection projection = new SystemProjectionDefault(provider.name(), provider.type(), provider, target);
    projections.put(projection.name(), projection);
    return projection;
  }

  private Object[] getProviderArguments(UnitProjectionProvider provider, Set<String> dependencyPath) {
    Method method = provider.providerMethod();
    Object[] arguments = new Object[method.getParameterCount()];
    int ind = 0;
    for (Parameter param : method.getParameters()) {
      if (dependencyPath.contains(param.getName())) {
        throw makeCyclicDependencyException(dependencyPath, param.getName());
      }
      Object target = getProjection(param.getName(), param.getType(), dependencyPath);
      if (target == null) {
        throw ConfigurationException.withMessage("Cannot to resolve parameter '{}' in method '{}' in unit {}",
            param.getName(), method.getName(), method.getDeclaringClass().getCanonicalName());
      }
      arguments[ind++] = target;
    }
    return arguments;
  }

  private ConfigurationException makeCyclicDependencyException(Set<String> dependencyPath, String nextProjection) {
    List<String> projections = new ArrayList<>(dependencyPath);
    projections.add(nextProjection);
    int cycleEndIndex = projections.size() - 1;
    int cycleBeginIndex = projections.size() - 2;
    while (!projections.get(cycleBeginIndex).equals(projections.get(cycleEndIndex))) {
      cycleBeginIndex--;
    }
    cycleEndIndex--;

    ProjectionProvider cycleBeginProvider = projectionProviders.get(projections.get(cycleBeginIndex));
    ProjectionProvider cycleEndProvider = projectionProviders.get(projections.get(cycleEndIndex));
    if (ProjectionProviderTypes.UnitMethod != cycleBeginProvider.providerType()) {
      throw new UnsupportedOperationException("Unsupported projection provider type - " + cycleBeginProvider.providerType());
    }
    if (ProjectionProviderTypes.UnitMethod != cycleEndProvider.providerType()) {
      throw new UnsupportedOperationException("Unsupported projection provider type - " + cycleEndProvider.providerType());
    }
    UnitProjectionProvider cycleBeginUnitProvider = (UnitProjectionProvider) cycleBeginProvider;
    UnitProjectionProvider cycleEndUnitProvider = (UnitProjectionProvider) cycleEndProvider;
    if (cycleBeginUnitProvider.unit().unitClass() == cycleEndUnitProvider.unit().unitClass()) {
      throw CyclicDependencyException.withMessage("Cyclic dependency between projections '{}' and '{}' in unit {}. Dependency path: {}",
          cycleBeginUnitProvider.name(), cycleEndUnitProvider.name(), cycleBeginUnitProvider.unit().unitClass().getCanonicalName(),
          buildDependencyPathExpression(projections.subList(cycleBeginIndex, cycleEndIndex + 2)));
    } else {
      throw CyclicDependencyException.withMessage("Cyclic dependency between projection '{}' in unit {} and projection '{}' in unit {}. Dependency path: {}",
          cycleBeginUnitProvider.name(), cycleBeginUnitProvider.unit().unitClass().getCanonicalName(),
          cycleEndUnitProvider.name(), cycleEndUnitProvider.unit().unitClass().getCanonicalName(),
          buildDependencyPathExpression(projections.subList(cycleBeginIndex, cycleEndIndex + 2)));
    }
  }

  private String buildDependencyPathExpression(List<String> dependencyPath) {
    return dependencyPath.stream()
        .map(value -> "'" + value + "'")
        .collect(Collectors.joining(" -> "));
  }
}
