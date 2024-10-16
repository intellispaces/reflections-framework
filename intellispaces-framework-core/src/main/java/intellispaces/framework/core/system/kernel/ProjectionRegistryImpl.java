package intellispaces.framework.core.system.kernel;

import intellispaces.common.action.Action;
import intellispaces.common.base.exception.ExceptionFunctions;
import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.framework.core.annotation.Projection;
import intellispaces.framework.core.exception.ConfigurationException;
import intellispaces.framework.core.exception.CyclicDependencyException;
import intellispaces.framework.core.object.ObjectFunctions;
import intellispaces.framework.core.system.ModuleProjection;
import intellispaces.framework.core.system.ProjectionDefinition;
import intellispaces.framework.core.system.ProjectionDefinitionKinds;
import intellispaces.framework.core.system.ProjectionTargetSupplier;
import intellispaces.framework.core.system.ProjectionReference;
import intellispaces.framework.core.system.UnitProjectionDefinition;
import intellispaces.framework.core.system.projection.ModuleProjectionImpl;
import intellispaces.framework.core.system.projection.ProjectionDefinitionBasedOnMethodAction;
import intellispaces.framework.core.system.projection.ProjectionDefinitionBasedOnProviderClass;
import intellispaces.framework.core.system.projection.ProjectionFunctions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
 * Implementation of the {@link ProjectionRegistry}.
 */
class ProjectionRegistryImpl implements ProjectionRegistry {
  private final Map<String, ProjectionDefinition> projectionDefinitions;
  private final Map<String, ModuleProjection> projectionsByName = new HashMap<>();

  private final ThreadLocal<Map<ModuleProjection, Object>> contextProjections = new ThreadLocal<>();
  private final ThreadLocal<Map<String, ModuleProjection>> contextProjectionsByName = new ThreadLocal<>();

  ProjectionRegistryImpl(List<ProjectionDefinition> projectionDefinitions) {
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
  public <T> T getProjection(String name, Class<T> targetObjectHandleClass) {
    if (!ObjectFunctions.isObjectHandleClass(targetObjectHandleClass)) {
      throw UnexpectedViolationException.withMessage("Expected target object handle class");
    }
    return getProjection(name, targetObjectHandleClass, new LinkedHashSet<>());
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> List<T> getProjections(Class<T> targetObjectHandleClass) {
    if (!ObjectFunctions.isObjectHandleClass(targetObjectHandleClass)) {
      throw UnexpectedViolationException.withMessage("Expected target object handle class");
    }

    List<T> projections = new ArrayList<>();
    for (ProjectionDefinition projectionDefinition : projectionDefinitions.values()) {
      if (projectionDefinition.type() == targetObjectHandleClass || targetObjectHandleClass.isAssignableFrom(projectionDefinition.type())) {
        T projection = (T) getProjection(projectionDefinition.name(), projectionDefinition.type());
        projections.add(projection);
      }
    }

    Map<ModuleProjection, Object> contextProjections = this.contextProjections.get();
    if (contextProjections != null) {
      for (ModuleProjection projection : contextProjections.keySet()) {
        if (projection.type() == targetObjectHandleClass || targetObjectHandleClass.isAssignableFrom(projection.type())) {
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
  public <T> void addContextProjection(String name, Class<T> targetObjectHandleClass, T target) {
    if (!ObjectFunctions.isObjectHandleClass(targetObjectHandleClass)) {
      throw UnexpectedViolationException.withMessage("Expected target object handle class");
    }

    ModuleProjection projection = new ModuleProjectionImpl(name, targetObjectHandleClass, null, target);

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
  private <T> T getProjection(String name, Class<T> targetObjectHandleClass, Set<String> dependencyPath) {
    ModuleProjection projection = getProjection(name, dependencyPath);
    if (projection == null) {
      return null;
    }
    if (!ObjectFunctions.isCompatibleObjectType(targetObjectHandleClass, projection.type())) {
      T downgradedProjection = ObjectFunctions.tryDowngrade(projection.target(), targetObjectHandleClass);
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
    if (ProjectionDefinitionKinds.ProjectionDefinitionBasedOnUnitMethod == definition.kind()) {
      return createProjection((ProjectionDefinitionBasedOnMethodAction) definition, dependencyPath);
    } else if (ProjectionDefinitionKinds.ProjectionDefinitionBasedOnProviderClass == definition.kind()) {
      return createProjection((ProjectionDefinitionBasedOnProviderClass) definition, dependencyPath);
    } else {
      throw new UnsupportedOperationException("Unsupported projection definition type: " + definition.type());
    }
  }

  private ModuleProjection createProjection(
      ProjectionDefinitionBasedOnMethodAction projectionDefinition, Set<String> dependencyPath
  ) {
    final Object target;
    try {
      Action action = projectionDefinition.methodAction();
      Object[] actionArguments = getProjectionArguments(projectionDefinition, dependencyPath);
      target = action.execute(actionArguments);
    } catch (Exception e) {
      throw ExceptionFunctions.coverIfChecked(e);
    }
    ModuleProjection projection = new ModuleProjectionImpl(
        projectionDefinition.name(), projectionDefinition.type(), projectionDefinition, target
    );
    projectionsByName.put(projection.name(), projection);
    return projection;
  }

  private ModuleProjection createProjection(
      ProjectionDefinitionBasedOnProviderClass projectionDefinition, Set<String> dependencyPath
  ) {
    Class<?> providerClass = TypeFunctions.getClass(projectionDefinition.providerClassCanonicalName())
        .orElseThrow(() -> UnexpectedViolationException.withMessage(
            "Could not find projection provider class be name {0}",
            projectionDefinition.providerClassCanonicalName()));

    Method projectionMethod = Arrays.stream(projectionDefinition.unitClass().getDeclaredMethods())
        .filter(m -> m.isAnnotationPresent(Projection.class))
        .filter(m -> projectionDefinition.name().equals(ProjectionFunctions.getProjectionName(m)))
        .findAny()
        .orElseThrow(() -> UnexpectedViolationException.withMessage(
            "Could not find projection method {0} in unit {1}",
            projectionDefinition.name(), projectionDefinition.unitClass().getCanonicalName()
        ));

    final ProjectionTargetSupplier provider;
    try {
      Constructor<?> providerConstructor = providerClass.getConstructor(Method.class);
      provider = (ProjectionTargetSupplier) providerConstructor.newInstance(projectionMethod);
    } catch (Exception e) {
      throw UnexpectedViolationException.withCauseAndMessage(e, "Failed to create projection provider");
    }

    final Object target;
    try {
      target = provider.get();
    } catch (Exception e) {
      throw UnexpectedViolationException.withCauseAndMessage(e, "Failed to call projection provider");
    }

    ModuleProjection projection = new ModuleProjectionImpl(
        projectionDefinition.name(), projectionDefinition.type(), projectionDefinition, target
    );
    projectionsByName.put(projection.name(), projection);
    return projection;
  }

  private Object[] getProjectionArguments(
      ProjectionDefinitionBasedOnMethodAction definition, Set<String> dependencyPath
  ) {
    List<ProjectionReference> requiredProjections = definition.requiredProjections();
    Object[] arguments = new Object[requiredProjections.size()];
    int ind = 0;
    for (ProjectionReference requiredProjection : requiredProjections) {
      if (dependencyPath.contains(requiredProjection.name())) {
        throw makeCyclicDependencyException(dependencyPath, requiredProjection.name());
      }
      Object target = getProjection(requiredProjection.name(), requiredProjection.targetClass(), dependencyPath);
      if (target == null) {
        throw ConfigurationException.withMessage("Cannot to resolve required projection ''{0}'' " +
                "in projection definition ''{1}'' of unit {2}",
            requiredProjection.name(), definition.name(), definition.unitClass().getCanonicalName());
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
    if (ProjectionDefinitionKinds.ProjectionDefinitionBasedOnUnitMethod != cycleFirstDefinition.kind()) {
      throw new UnsupportedOperationException("Unsupported projection definition type: " + cycleFirstDefinition.kind());
    }
    if (ProjectionDefinitionKinds.ProjectionDefinitionBasedOnUnitMethod != cycleLastDefinition.kind()) {
      throw new UnsupportedOperationException("Unsupported projection definition type: " + cycleLastDefinition.kind());
    }
    UnitProjectionDefinition cycleFirstUnitDefinition = (UnitProjectionDefinition) cycleFirstDefinition;
    UnitProjectionDefinition cycleLastUnitDefinition = (UnitProjectionDefinition) cycleLastDefinition;
    if (cycleFirstUnitDefinition.unitClass() == cycleLastUnitDefinition.unitClass()) {
      throw CyclicDependencyException.withMessage(
          "Cyclic dependency between projections ''{0}'' and ''{1}'' in unit {2}. Dependency path: {3}",
          cycleFirstUnitDefinition.name(), cycleLastUnitDefinition.name(),
          cycleFirstUnitDefinition.unitClass().getCanonicalName(),
          buildDependencyPathExpression(projections.subList(cycleBeginIndex, cycleEndIndex + 2)));
    } else {
      throw CyclicDependencyException.withMessage(
          "Cyclic dependency between projection ''{0}'' in unit {1} and projection ''{2}'' in unit {3}. " +
              "Dependency path: {4}",
          cycleFirstUnitDefinition.name(), cycleFirstUnitDefinition.unitClass().getCanonicalName(),
          cycleLastUnitDefinition.name(), cycleLastUnitDefinition.unitClass().getCanonicalName(),
          buildDependencyPathExpression(projections.subList(cycleBeginIndex, cycleEndIndex + 2)));
    }
  }

  private String buildDependencyPathExpression(List<String> dependencyPath) {
    return dependencyPath.stream()
        .map(value -> "'" + value + "'")
        .collect(Collectors.joining(" -> "));
  }
}
