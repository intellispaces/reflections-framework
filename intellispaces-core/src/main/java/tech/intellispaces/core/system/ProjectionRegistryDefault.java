package tech.intellispaces.core.system;

import tech.intellispaces.commons.exception.ExceptionFunctions;
import tech.intellispaces.core.exception.ConfigurationException;
import tech.intellispaces.core.exception.CyclicDependencyException;
import tech.intellispaces.core.object.ObjectFunctions;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
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
  private final Map<String, ProjectionDefinition> projectionDefinitions;
  private final Map<String, ModuleProjection> projectionsByName = new HashMap<>();

  private final ThreadLocal<Map<ModuleProjection, Object>> contextProjections = new ThreadLocal<>();
  private final ThreadLocal<Map<String, ModuleProjection>> contextProjectionsByName = new ThreadLocal<>();

  ProjectionRegistryDefault(List<ProjectionDefinition> projectionDefinitions) {
    this.projectionDefinitions = projectionDefinitions.stream().collect(
        Collectors.toMap(ProjectionDefinition::name, Function.identity()));
  }

  @Override
  public void load() {
    projectionDefinitions.values().stream()
        .filter(definition -> !definition.isLazy())
        .forEach(definition -> createProjection(definition, new LinkedHashSet<>()));
  }

  @Override
  public <T> T getProjection(String name, Class<T> targetClass) {
    return getProjection(name, targetClass, new LinkedHashSet<>());
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> List<T> getProjections(Class<T> targetClass) {
    List<T> projections = new ArrayList<>();
    for (ProjectionDefinition projectionDefinition : projectionDefinitions.values()) {
      if (projectionDefinition.type() == targetClass || targetClass.isAssignableFrom(projectionDefinition.type())) {
        T projection = (T) getProjection(projectionDefinition.name(), projectionDefinition.type());
        projections.add(projection);
      }
    }

    Map<ModuleProjection, Object> contextProjections = this.contextProjections.get();
    if (contextProjections != null) {
      for (ModuleProjection projection : contextProjections.keySet()) {
        if (projection.type() == targetClass || targetClass.isAssignableFrom(projection.type())) {
          projections.add((T) projection.target());
        }
      }
    }
    return projections;
  }

  @Override
  public Collection<ModuleProjection> allProjections() {
    List<ModuleProjection> projections = new ArrayList<>();
    Set<String> dependencyPath = new HashSet<>();
    for (ProjectionDefinition projectionDefinition : projectionDefinitions.values()) {
      ModuleProjection projection = getProjection(projectionDefinition.name(), dependencyPath);
      projections.add(projection);
    }

    Map<ModuleProjection, Object> contextProjections = this.contextProjections.get();
    if (contextProjections != null) {
      projections.addAll(contextProjections.keySet());
    }
    return projections;
  }

  @Override
  public <T> void addContextProjection(String name, Class<T> type, T target) {
    ModuleProjection projection = new ModuleProjectionImpl(name, type, null, target);

    Map<String, ModuleProjection> mapByName = contextProjectionsByName.get();
    if (mapByName == null) {
      mapByName = new HashMap<>();
      contextProjectionsByName.set(mapByName);
    }
    mapByName.put(name, projection);

    Map<ModuleProjection, Object> allContextProjections = contextProjections.get();
    if (allContextProjections == null) {
      allContextProjections = new IdentityHashMap<>();
      contextProjections.set(allContextProjections);
    }
    allContextProjections.put(projection, Boolean.TRUE);
  }

  @Override
  public void removeContextProjection(String name) {
    Map<String, ModuleProjection> mapByName = contextProjectionsByName.get();
    if (mapByName != null) {
      ModuleProjection projection = mapByName.remove(name);
      if (projection != null) {
        Map<ModuleProjection, Object> allContextProjections = contextProjections.get();
        if (allContextProjections != null) {
          allContextProjections.remove(projection);
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private <T> T getProjection(String name, Class<T> targetClass, Set<String> dependencyPath) {
    ModuleProjection projection = getProjection(name, dependencyPath);
    if (projection == null) {
      return null;
    }
    if (!ObjectFunctions.isCompatibleObjectType(targetClass, projection.type())) {
      T downgradedProjection = ObjectFunctions.tryDowngrade(projection.target(), targetClass);
      if (downgradedProjection != null) {
        return downgradedProjection;
      }
      return null;
    }
    return (T) projection.target();
  }

  private <T> ModuleProjection getProjection(String name, Set<String> dependencyPath) {
    ModuleProjection projection = projectionsByName.get(name);
    if (projection == null) {
      projection = getContextProjection(name);
    }
    if (projection == null) {
      projection = defineProjection(name, dependencyPath);
    }
    if (projection == null) {
      return null;
    }
    return projection;
  }

  private ModuleProjection getContextProjection(String name) {
    Map<String, ModuleProjection> contextProjections = contextProjectionsByName.get();
    if (contextProjections == null) {
      return null;
    }
    return contextProjections.get(name);
  }

  private ModuleProjection defineProjection(String name, Set<String> dependencyPath) {
    ProjectionDefinition definition = projectionDefinitions.get(name);
    if (definition == null) {
      return null;
    }
    return createProjection(definition, dependencyPath);
  }

  private ModuleProjection createProjection(ProjectionDefinition definition, Set<String> dependencyPath) {
    dependencyPath.add(definition.name());
    if (ProjectionDefinitionTypes.UnitMethod == definition.kind()) {
      return createUnitProjection((UnitProjectionDefinition) definition, dependencyPath);
    } else {
      throw new UnsupportedOperationException("Unsupported projection definition type: " + definition.type());
    }
  }

  private ModuleProjection createUnitProjection(
      UnitProjectionDefinition definition, Set<String> dependencyPath
  ) {
    final Object target;
    try {
      Method projectionMethod = definition.projectionMethod();
      Object[] projectionArguments = getProjectionArguments(definition, dependencyPath);
      target = projectionMethod.invoke(definition.unit().instance(), projectionArguments);
    } catch (Exception e) {
      throw ExceptionFunctions.coverIfChecked(e);
    }
    ModuleProjection projection = new ModuleProjectionImpl(definition.name(), definition.type(), definition, target);
    projectionsByName.put(projection.name(), projection);
    return projection;
  }

  private Object[] getProjectionArguments(
      UnitProjectionDefinition definition, Set<String> dependencyPath
  ) {
    Method method = definition.projectionMethod();
    Object[] arguments = new Object[method.getParameterCount()];
    int ind = 0;
    for (Parameter param : method.getParameters()) {
      if (dependencyPath.contains(param.getName())) {
        throw makeCyclicDependencyException(dependencyPath, param.getName());
      }
      Object target = getProjection(param.getName(), param.getType(), dependencyPath);
      if (target == null) {
        getProjection(param.getName(), param.getType(), dependencyPath);


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
