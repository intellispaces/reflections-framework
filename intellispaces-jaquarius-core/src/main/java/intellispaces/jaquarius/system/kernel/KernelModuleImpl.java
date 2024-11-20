package intellispaces.jaquarius.system.kernel;

import intellispaces.jaquarius.system.Module;
import intellispaces.jaquarius.system.Unit;
import intellispaces.jaquarius.traverse.plan.TraverseAnalyzer;
import intellispaces.jaquarius.traverse.plan.TraverseExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.intellispaces.action.cache.CachedSupplierActions;
import tech.intellispaces.action.supplier.SupplierAction;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation of the {@link KernelModule}.
 */
class KernelModuleImpl implements KernelModule {
  private final Module module;
  private final List<KernelUnit> units;
  private final ObjectRegistry objectRegistry;
  private final ProjectionRegistry projectionRegistry;
  private final GuideRegistry guideRegistry;
  private final TraverseAnalyzer traverseAnalyzer;
  private final TraverseExecutor traverseExecutor;

  private final AtomicBoolean started = new AtomicBoolean(false);
  private final SupplierAction<KernelUnit> mainUnitGetter = CachedSupplierActions.get(
      this::mainUnitSupplier
  );

  private static final Logger LOG = LoggerFactory.getLogger(KernelModuleImpl.class);

  KernelModuleImpl(
      List<KernelUnit> units,
      ObjectRegistry objectRegistry,
      ProjectionRegistry projectionRegistry,
      GuideRegistry guideRegistry,
      TraverseAnalyzer traverseAnalyzer,
      TraverseExecutor traverseExecutor
  ) {
    this.module = new ModuleImpl(this);
    this.units = List.copyOf(units);
    this.objectRegistry = objectRegistry;
    this.projectionRegistry = projectionRegistry;
    this.guideRegistry = guideRegistry;
    this.traverseAnalyzer = traverseAnalyzer;
    this.traverseExecutor = traverseExecutor;
  }

  @Override
  public void start(String[] args) {
    ModuleStarterFunctions.startModule(this);
    started.set(true);
  }

  @Override
  public void stop() {
    if (started.compareAndSet(true, false)) {

    } else {
      LOG.warn("Module is already stopped");
    }
  }

  @Override
  public Module module() {
    return module;
  }

  @Override
  public ObjectRegistry objectRegistry() {
    return objectRegistry;
  }

  @Override
  public ProjectionRegistry projectionRegistry() {
    return projectionRegistry;
  }

  @Override
  public GuideRegistry guideRegistry() {
    return guideRegistry;
  }

  @Override
  public TraverseAnalyzer traverseAnalyzer() {
    return traverseAnalyzer;
  }

  @Override
  public TraverseExecutor traverseExecutor() {
    return traverseExecutor;
  }

  @Override
  public KernelUnit mainUnit() {
    return mainUnitGetter.get();
  }

  private KernelUnit mainUnitSupplier() {
    return units.stream()
        .filter(Unit::isMain)
        .findFirst()
        .orElseThrow();
  }

  @Override
  public List<KernelUnit> units() {
    return units;
  }
}
