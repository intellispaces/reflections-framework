package intellispaces.framework.core.system.kernel;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.system.Module;
import intellispaces.framework.core.system.Modules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

public final class KernelModules {
  private static final AtomicReference<KernelModule> CURRENT = new AtomicReference<>();
  private static final Logger LOG = LoggerFactory.getLogger(Modules.class);

  private KernelModules() {}

  public static KernelModule current() {
    KernelModule module = currentSilently();
    if (module == null) {
      throw UnexpectedViolationException.withMessage("Current module is not defined. " +
          "It is possible that the module is not loaded yet");
    }
    return module;
  }

  public static KernelModule currentSilently() {
    return CURRENT.get();
  }

  public static void setCurrentModule(KernelModule module) {
    Module previous = CURRENT.getAndSet(module);
    if (previous != null && module != null) {
      LOG.warn("Current module has been changed");
    }
  }
}
