package tech.intellispaces.reflections.framework.system;

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

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.exception.ExceptionFunctions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.reflections.framework.annotation.Projection;
import tech.intellispaces.reflections.framework.exception.ConfigurationException;
import tech.intellispaces.reflections.framework.exception.ConfigurationExceptions;
import tech.intellispaces.reflections.framework.exception.CyclicDependencyExceptions;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.system.projection.DirectProjectionDefinition;
import tech.intellispaces.reflections.framework.system.projection.ModuleProjectionImpl;
import tech.intellispaces.reflections.framework.system.projection.ProjectionDefinitionKinds;
import tech.intellispaces.reflections.framework.system.projection.ProjectionFunctions;
import tech.intellispaces.reflections.framework.system.projection.ProviderClassProjectionDefinition;
import tech.intellispaces.reflections.framework.system.projection.UnitMethodProjectionDefinition;

/**
 * The local projection register implementation.
 */
public class LocalProjectionRegistry implements ProjectionRegistry {
  private final Map<String, ProjectionDefinition> projectionDefinitionIndexByName = new HashMap<>();
  private final Map<Class<?>, ProjectionDefinition> projectionDefinitionIndexByType = new HashMap<>();
  private final Map<String, ModuleProjection> projectionIndexByName = new HashMap<>();
  private final Map<Class<?>, ModuleProjection> projectionIndexByType = new HashMap<>();

  private final ThreadLocal<Map<ModuleProjection, Object>> contextProjections = new ThreadLocal<>();
  private final ThreadLocal<Map<String, ModuleProjection>> contextProjectionsByName = new ThreadLocal<>();

  public LocalProjectionRegistry() {
  }

  public LocalProjectionRegistry(List<ProjectionDefinition> projectionDefinitions) {
    projectionDefinitionIndexByName.putAll(
        projectionDefinitions.stream().collect(Collectors.toMap(ProjectionDefinition::name, Function.identity()))
    );
    projectionDefinitionIndexByType.putAll(
        projectionDefinitions.stream().collect(Collectors.toMap(ProjectionDefinition::type, Function.identity()))
    );
  }

  @Override
  public void onStartup() {
    projectionDefinitionIndexByName.values().stream()
        .filter(definition -> !definition.isLazy())
        .forEach(definition -> defineProjection(definition, new LinkedHashSet<>()));
  }

  @Override
  public void onShutdown() {
    // do nothing
  }

  @Override
  public void addProjection(ProjectionDefinition projectionDefinition) {
    projectionDefinitionIndexByName.put(projectionDefinition.name(), projectionDefinition);
    projectionDefinitionIndexByType.put(projectionDefinition.type(), projectionDefinition);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> List<T> findProjections(Class<T> targetClass) {
    List<T> projections = new ArrayList<>();
    for (ProjectionDefinition projectionDefinition : projectionDefinitionIndexByName.values()) {
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
  public Collection<ModuleProjection> moduleProjections() {
    List<ModuleProjection> projections = new ArrayList<>();
    Set<String> dependencyPath = new HashSet<>();
    for (ProjectionDefinition pd : projectionDefinitionIndexByName.values()) {
      ModuleProjection projection = findProjection(pd.name(), pd.type(), dependencyPath);
      projections.add(projection);
    }

    Map<ModuleProjection, Object> contextProjections = this.contextProjections.get();
    if (contextProjections != null) {
      projections.addAll(contextProjections.keySet());
    }
    return projections;
  }

  @Override
  public <T> void addContextProjection(String name, Class<T> targetClass, T target) {
    ModuleProjection projection = new ModuleProjectionImpl(name, targetClass, null, target);

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

  @Override
  public <T> T getProjection(String name, Class<T> type) {
    return getProjection(name, type, new LinkedHashSet<>());
  }

  @SuppressWarnings("unchecked")
  private <T> T getProjection(String name, Class<T> type, Set<String> dependencyPath) {
    ModuleProjection projection = findProjection(name, type, dependencyPath);
    if (projection == null) {
      return null;
    }
    if (projection.target() != null && ClassFunctions.isCompatibleClasses(type, projection.target().getClass())) {
      return (T) projection.target();
    }
    if (!ClassFunctions.isCompatibleClasses(type, projection.type())) {
      T downgradedProjection = ReflectionFunctions.tryDowngrade(projection.target(), type);
      if (downgradedProjection != null) {
        return downgradedProjection;
      }
      return null;
    }
    return (T) projection.target();
  }

  private ModuleProjection findProjection(String name, Class<?> type, Set<String> dependencyPath) {
    if (StringFunctions.isNotBlank(name)) {
      ModuleProjection projection = projectionIndexByName.get(name);
      if (projection == null) {
        projection = getContextProjection(name);
        if (projection != null) {
          return projection;
        }
      }
    } else if (type != null) {
      ModuleProjection projection = projectionIndexByType.get(type);
      if (projection != null) {
        return projection;
      }
    }

    ModuleProjection projection = defineProjection(name, type, dependencyPath);
    projectionIndexByName.put(name, projection);
    projectionIndexByType.put(type, projection);
    return projection;
  }

  private ModuleProjection getContextProjection(String name) {
    Map<String, ModuleProjection> contextProjections = contextProjectionsByName.get();
    if (contextProjections == null) {
      return null;
    }
    return contextProjections.get(name);
  }

  private ModuleProjection defineProjection(String name, Class<?> type, Set<String> dependencyPath) {
    ProjectionDefinition definition = findProjectionDefinition(name, type);
    if (definition == null) {
      return null;
    }
    return defineProjection(definition, dependencyPath);
  }

  private ProjectionDefinition findProjectionDefinition(String name, Class<?> type) {
    if (StringFunctions.isNotBlank(name)) {
      return projectionDefinitionIndexByName.get(name);
    }
    if (type != null) {
      return projectionDefinitionIndexByType.get(type);
    }
    return null;
  }

  private ModuleProjection defineProjection(ProjectionDefinition definition, Set<String> dependencyPath) {
    dependencyPath.add(definition.name());
    if (ProjectionDefinitionKinds.UnitMethodProjectionDefinition.is(definition.kind())) {
      return defineProjection((UnitMethodProjectionDefinition) definition, dependencyPath);
    } else if (ProjectionDefinitionKinds.ProviderClassProjectionDefinition.is(definition.kind())) {
      return defineProjection((ProviderClassProjectionDefinition) definition, dependencyPath);
    } else if (ProjectionDefinitionKinds.DirectProjectionDefinition.is(definition.kind())) {
      return defineProjection((DirectProjectionDefinition) definition, dependencyPath);
    } else {
      throw new UnsupportedOperationException("Unsupported projection definition type: " + definition.type());
    }
  }

  private ModuleProjection defineProjection(
      UnitMethodProjectionDefinition projectionDefinition, Set<String> dependencyPath
  ) {
    final Object target;
    try {
      Action action = projectionDefinition.methodAction();
      Object[] actionArguments = getProjectionArguments(projectionDefinition, dependencyPath);
      target = action.execute(actionArguments);
    } catch (Exception e) {
      throw ExceptionFunctions.wrapIfChecked(e);
    }
    ModuleProjection projection = new ModuleProjectionImpl(
        projectionDefinition.name(), projectionDefinition.type(), projectionDefinition, target
    );
    projectionIndexByName.put(projection.name(), projection);
    return projection;
  }

  private ModuleProjection defineProjection(
      ProviderClassProjectionDefinition projectionDefinition, Set<String> dependencyPath
  ) {
    Class<?> providerClass = ClassFunctions.getClass(projectionDefinition.providerClassCanonicalName())
        .orElseThrow(() -> UnexpectedExceptions.withMessage(
            "Could not find projection provider class be name {0}",
            projectionDefinition.providerClassCanonicalName()));

    Method projectionMethod = Arrays.stream(projectionDefinition.unitClass().getDeclaredMethods())
        .filter(m -> m.isAnnotationPresent(Projection.class))
        .filter(m -> projectionDefinition.name().equals(ProjectionFunctions.getProjectionName(m)))
        .findAny()
        .orElseThrow(() -> UnexpectedExceptions.withMessage(
            "Could not find projection method {0} in unit {1}",
            projectionDefinition.name(), projectionDefinition.unitClass().getCanonicalName()
        ));

    final ProjectionSupplier provider;
    try {
      Constructor<?> providerConstructor = providerClass.getConstructor(Method.class);
      provider = (ProjectionSupplier) providerConstructor.newInstance(projectionMethod);
    } catch (Exception e) {
      throw UnexpectedExceptions.withCauseAndMessage(e, "Failed to create projection provider");
    }

    final Object target;
    try {
      target = provider.get();
    } catch (Exception e) {
      throw UnexpectedExceptions.withCauseAndMessage(e, "Failed to call projection provider");
    }

    ModuleProjection projection = new ModuleProjectionImpl(
        projectionDefinition.name(), projectionDefinition.type(), projectionDefinition, target
    );
    projectionIndexByName.put(projection.name(), projection);
    return projection;
  }

  private ModuleProjection defineProjection(
      DirectProjectionDefinition projectionDefinition, Set<String> dependencyPath
  ) {
    ModuleProjection projection = new ModuleProjectionImpl(
        projectionDefinition.name(),
        projectionDefinition.type(),
        projectionDefinition,
        projectionDefinition.target()
    );
    projectionIndexByName.put(projection.name(), projection);
    return projection;
  }

  private Object[] getProjectionArguments(
      UnitMethodProjectionDefinition definition, Set<String> dependencyPath
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
        throw ConfigurationExceptions.withMessage("Cannot to resolve required projection '{0}' " +
                "in projection definition '{1}' of unit {2}",
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

    ProjectionDefinition cycleFirstDefinition = projectionDefinitionIndexByName.get(projections.get(cycleBeginIndex));
    ProjectionDefinition cycleLastDefinition = projectionDefinitionIndexByName.get(projections.get(cycleEndIndex));
    if (ProjectionDefinitionKinds.UnitMethodProjectionDefinition.isNot(cycleFirstDefinition.kind())) {
      throw new UnsupportedOperationException("Unsupported projection definition type: " + cycleFirstDefinition.kind());
    }
    if (ProjectionDefinitionKinds.UnitMethodProjectionDefinition.isNot(cycleLastDefinition.kind())) {
      throw new UnsupportedOperationException("Unsupported projection definition type: " + cycleLastDefinition.kind());
    }
    UnitProjectionDefinition cycleFirstUnitDefinition = (UnitProjectionDefinition) cycleFirstDefinition;
    UnitProjectionDefinition cycleLastUnitDefinition = (UnitProjectionDefinition) cycleLastDefinition;
    if (cycleFirstUnitDefinition.unitClass() == cycleLastUnitDefinition.unitClass()) {
      throw CyclicDependencyExceptions.withMessage(
          "Cyclic dependency between projections '{0}' and '{1}' in unit {2}. Dependency path: {3}",
          cycleFirstUnitDefinition.name(), cycleLastUnitDefinition.name(),
          cycleFirstUnitDefinition.unitClass().getCanonicalName(),
          buildDependencyPathExpression(projections.subList(cycleBeginIndex, cycleEndIndex + 2)));
    } else {
      throw CyclicDependencyExceptions.withMessage(
          "Cyclic dependency between projection '{0}' in unit {1} and projection '{2}' in unit {3}. " +
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
