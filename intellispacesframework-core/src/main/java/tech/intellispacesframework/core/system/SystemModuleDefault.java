package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.action.ActionBuilders;
import tech.intellispacesframework.commons.action.Getter;
import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.exception.ConfigurationException;
import tech.intellispacesframework.core.guide.AutoMover1;
import tech.intellispacesframework.core.guide.n1.Mover1;
import tech.intellispacesframework.core.traverseplan.TraversePlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Default implementation of the {@link SystemModule}.
 */
class SystemModuleDefault implements SystemModule {
  private final List<SystemUnit> units;
  private final ProjectionRegistry projectionRegistry;
  private final TraverseAnalyzer traverseAnalyzer;

  private final AtomicBoolean started = new AtomicBoolean();
  private final Getter<SystemUnit> mainUnitGetter = ActionBuilders.cachedLazyGetter(this::mainUnit);

  private static final Logger LOG = LoggerFactory.getLogger(SystemModuleDefault.class);

  public SystemModuleDefault(
      List<SystemUnit> units,
      ProjectionRegistry projectionRegistry,
      TraverseAnalyzer traverseAnalyzer
  ) {
    this.units = List.copyOf(units);
    this.projectionRegistry = projectionRegistry;
    this.traverseAnalyzer = traverseAnalyzer;
  }

  @Override
  public List<SystemUnit> units() {
    return units;
  }

  public ProjectionRegistry projectionRegistry() {
    return projectionRegistry;
  }

  public TraverseAnalyzer traverseAnalyzer() {
    return traverseAnalyzer;
  }

  @Override
  public SystemModule start() {
    if (started.compareAndSet(false, true)) {
      startInternal();
    } else {
      LOG.warn("Module is already started");
    }
    return this;
  }

  @Override
  public void shutdown() {
    if (started.compareAndSet(true, false)) {

    }
    LOG.warn("Module is already shutdown");
  }

  @Override
  public boolean isStarted() {
    return started.get();
  }

  @Override
  public <T> Optional<T> projection(String name, Class<T> targetClass) {
    return projectionRegistry.getProjection(name, targetClass);
  }

  @Override
  public <S, Q> Mover1<S, Q> autoMoverThruTransition1(Class<S> sourceClass, String tid) {
    TraversePlan traversePlan = traverseAnalyzer.buildTraversePlanMoveObjectHandleThruTransition1(sourceClass, tid);
    return new AutoMover1<>(traversePlan);
  }

  @Override
  public <S, Q> S moveThruTransition1(S source, String tid, Q qualifier) {
    return null;
  }

  private void startInternal() {
    invokeStartupMethod();
  }
  
  private void invokeStartupMethod() {
    SystemUnit mainUnit = mainUnitGetter.get();
    if (mainUnit.startupMethod().isPresent()) {
      Method startupMethod = mainUnit.startupMethod().get();
      Object[] arguments = prepareMethodArguments(startupMethod);
      try {
        startupMethod.invoke(mainUnit.instance(), arguments);
      } catch (Exception e) {
        throw UnexpectedViolationException.withCauseAndMessage(e, "Can't to invoke module startup method {}", startupMethod.getName());
      }
    }
  }

  private Object[] prepareMethodArguments(Method method) {
    var arguments = new ArrayList<>();
    for (Parameter param : method.getParameters()) {
      Optional<?> projection = projectionRegistry.getProjection(param.getName(), param.getType());
      if (projection.isEmpty()) {
        throw ConfigurationException.withMessage("Cannot to resolve parameter '{}' in method {}#{}",
            param.getName(), method.getDeclaringClass().getSimpleName(), method.getName());
      }
      arguments.add(projection.get());
    }
    return arguments.toArray();
  }

  private SystemUnit mainUnit() {
    return units.stream()
        .filter(SystemUnit::isMain)
        .findFirst()
        .orElseThrow();
  }
}
