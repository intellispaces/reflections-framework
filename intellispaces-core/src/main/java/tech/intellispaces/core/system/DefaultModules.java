package tech.intellispaces.core.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.intellispaces.commons.exception.UnexpectedViolationException;

import java.util.concurrent.atomic.AtomicReference;

public final class DefaultModules {
  private static final AtomicReference<ModuleDefault> CURRENT = new AtomicReference<>();
  private static final Logger LOG = LoggerFactory.getLogger(Modules.class);

  private DefaultModules() {}

  public static ModuleDefault current() {
    ModuleDefault module = currentSilently();
    if (module == null) {
      throw UnexpectedViolationException.withMessage("Current module is not defined. " +
          "It is possible that the module is not loaded yet");
    }
    return module;
  }

  public static ModuleDefault currentSilently() {
    return CURRENT.get();
  }

  static void setCurrentModule(ModuleDefault module) {
    Module previous = CURRENT.getAndSet(module);
    if (previous != null && module != null) {
      LOG.warn("Current module has been changed");
    }
  }
}
