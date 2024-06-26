package tech.intellispaces.framework.core.system;

import tech.intellispaces.framework.commons.exception.ExceptionFunctions;
import tech.intellispaces.framework.core.exception.ConfigurationException;
import tech.intellispaces.framework.core.exception.CyclicDependencyException;
import tech.intellispaces.framework.core.object.ObjectFunctions;

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
 * Implementation of the {@link ProjectionRegistry}.
 */
class ProjectionRegistryImpl implements ProjectionRegistry {
  private final Map<String, ProjectionDefinition> projectionDefinitions;
  private final Map<String, SystemProjection> projections = new HashMap<>();

  ProjectionRegistryImpl(List<ProjectionDefinition> projectionDefinitions) {
    this.projectionDefinitions = projectionDefinitions.stream().collect(
        Collectors.toMap(ProjectionDefinition::name, Function.identity()));
  }

  @Override
  public <T> T getProjectionTarget(String name, Class<T> targetClass) {
    return getProjection(name, targetClass, new LinkedHashSet<>());
  }

  @Override
  public Collection<SystemProjection> projections() {
    return Collections.unmodifiableCollection(projections.values());
  }

  @Override
  public void load() {
    projectionDefinitions.values().stream()
        .filter(definition -> !definition.isLazy())
        .forEach(definition -> createProjection(definition, new LinkedHashSet<>()));
  }

  @SuppressWarnings("unchecked")
  private <T> T getProjection(String name, Class<T> targetClass, Set<String> dependencyPath) {
    SystemProjection projection = findProjectionByName(name);
    if (projection == null) {
      projection = defineProjection(name, dependencyPath);
    }
    if (projection == null) {
      return null;
    }
    if (!ObjectFunctions.isCompatibleObjectType(targetClass, projection.targetClass())) {
      return null;
    }
    return (T) projection.target();
  }

  private SystemProjection findProjectionByName(String name) {
    return projections.get(name);
  }

  private SystemProjection defineProjection(String name, Set<String> dependencyPath) {
    ProjectionDefinition definition = projectionDefinitions.get(name);
    if (definition == null) {
      return null;
    }
    return createProjection(definition, dependencyPath);
  }

  private SystemProjection createProjection(ProjectionDefinition definition, Set<String> dependencyPath) {
    dependencyPath.add(definition.name());
    if (ProjectionDefinitionTypes.UnitMethod == definition.kind()) {
      return createUnitProjection((UnitProjectionDefinition) definition, dependencyPath);
    } else {
      throw new UnsupportedOperationException("Unsupported projection definition type: " + definition.type());
    }
  }

  private SystemProjection createUnitProjection(UnitProjectionDefinition definition, Set<String> dependencyPath) {
    final Object target;
    try {
      Method projectionMethod = definition.projectionMethod();
      Object[] projectionArguments = getProjectionArguments(definition, dependencyPath);
      target = projectionMethod.invoke(definition.unit().instance(), projectionArguments);
    } catch (Exception e) {
      throw ExceptionFunctions.coverException(e);
    }
    SystemProjection projection = new SystemProjectionImpl(definition.name(), definition.type(), definition, target);
    projections.put(projection.name(), projection);
    return projection;
  }

  private Object[] getProjectionArguments(UnitProjectionDefinition definition, Set<String> dependencyPath) {
    Method method = definition.projectionMethod();
    Object[] arguments = new Object[method.getParameterCount()];
    int ind = 0;
    for (Parameter param : method.getParameters()) {
      if (dependencyPath.contains(param.getName())) {
        throw makeCyclicDependencyException(dependencyPath, param.getName());
      }
      Object target = getProjection(param.getName(), param.getType(), dependencyPath);
      if (target == null) {
        Method actualMethod = UnitWrapper.getActualMethod(method);
        throw ConfigurationException.withMessage("Cannot to resolve parameter '{}' in method '{}' of unit {}",
            param.getName(), actualMethod.getName(), actualMethod.getDeclaringClass().getCanonicalName());
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

    ProjectionDefinition cycleFirstDefinition = projectionDefinitions.get(projections.get(cycleBeginIndex));
    ProjectionDefinition cycleLastDefinition = projectionDefinitions.get(projections.get(cycleEndIndex));
    if (ProjectionDefinitionTypes.UnitMethod != cycleFirstDefinition.kind()) {
      throw new UnsupportedOperationException("Unsupported projection definition type: " + cycleFirstDefinition.kind());
    }
    if (ProjectionDefinitionTypes.UnitMethod != cycleLastDefinition.kind()) {
      throw new UnsupportedOperationException("Unsupported projection definition type: " + cycleLastDefinition.kind());
    }
    UnitProjectionDefinition cycleFirstUnitDefinition = (UnitProjectionDefinition) cycleFirstDefinition;
    UnitProjectionDefinition cycleLastUnitDefinition = (UnitProjectionDefinition) cycleLastDefinition;
    if (cycleFirstUnitDefinition.unit().unitClass() == cycleLastUnitDefinition.unit().unitClass()) {
      throw CyclicDependencyException.withMessage(
          "Cyclic dependency between projections '{}' and '{}' in unit {}. Dependency path: {}",
          cycleFirstUnitDefinition.name(), cycleLastUnitDefinition.name(),
          cycleFirstUnitDefinition.unit().unitClass().getCanonicalName(),
          buildDependencyPathExpression(projections.subList(cycleBeginIndex, cycleEndIndex + 2)));
    } else {
      throw CyclicDependencyException.withMessage(
          "Cyclic dependency between projection '{}' in unit {} and projection '{}' in unit {}. Dependency path: {}",
          cycleFirstUnitDefinition.name(), cycleFirstUnitDefinition.unit().unitClass().getCanonicalName(),
          cycleLastUnitDefinition.name(), cycleLastUnitDefinition.unit().unitClass().getCanonicalName(),
          buildDependencyPathExpression(projections.subList(cycleBeginIndex, cycleEndIndex + 2)));
    }
  }

  private String buildDependencyPathExpression(List<String> dependencyPath) {
    return dependencyPath.stream()
        .map(value -> "'" + value + "'")
        .collect(Collectors.joining(" -> "));
  }
}
