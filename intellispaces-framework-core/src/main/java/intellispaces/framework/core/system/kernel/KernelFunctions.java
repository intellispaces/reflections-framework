package intellispaces.framework.core.system.kernel;

import intellispaces.common.base.exception.UnexpectedViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

public final class KernelFunctions {
  private static final AtomicReference<KernelModule> CURRENT_MODULE = new AtomicReference<>();
  private static final Logger LOG = LoggerFactory.getLogger(KernelFunctions.class);

  private KernelFunctions() {}

  public static KernelModule currentModule() {
    KernelModule module = currentModuleSilently();
    if (module == null) {
      throw UnexpectedViolationException.withMessage("Current module is not defined. " +
          "It is possible that the module is not loaded yet");
    }
    return module;
  }

  public static KernelModule currentModuleSilently() {
    return CURRENT_MODULE.get();
  }

  public static void setCurrentModule(KernelModule module) {
    KernelModule previous = CURRENT_MODULE.getAndSet(module);
    if (previous != null && module != null) {
      LOG.warn("Current module has been changed");
    }
  }
}
