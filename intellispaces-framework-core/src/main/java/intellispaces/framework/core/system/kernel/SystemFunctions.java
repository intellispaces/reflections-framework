package intellispaces.framework.core.system.kernel;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.system.Module;
import intellispaces.framework.core.system.Modules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

public final class SystemFunctions {
  private static final AtomicReference<SystemModule> CURRENT = new AtomicReference<>();
  private static final Logger LOG = LoggerFactory.getLogger(Modules.class);

  private SystemFunctions() {}

  public static SystemModule currentModule() {
    SystemModule module = currentModuleSilently();
    if (module == null) {
      throw UnexpectedViolationException.withMessage("Current module is not defined. " +
          "It is possible that the module is not loaded yet");
    }
    return module;
  }

  public static SystemModule currentModuleSilently() {
    return CURRENT.get();
  }

  public static void setCurrentModule(SystemModule module) {
    Module previous = CURRENT.getAndSet(module);
    if (previous != null && module != null) {
      LOG.warn("Current module has been changed");
    }
  }
}
