package tech.intellispaces.core.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.intellispaces.commons.exception.UnexpectedViolationException;

import java.util.concurrent.atomic.AtomicReference;

public class Modules {
  private static final AtomicReference<ModuleDefault> ACTIVE = new AtomicReference<>();
  private static final Logger LOG = LoggerFactory.getLogger(Modules.class);

  private Modules() {}

  public static ModuleDefault current() {
    ModuleDefault module = currentSilently();
    if (module == null) {
      throw UnexpectedViolationException.withMessage("Current module is not defined. " +
          "It is possible that the module is not loaded yet");
    }
    return module;
  }

  static ModuleDefault currentSilently() {
    return ACTIVE.get();
  }

  static void setCurrentModule(ModuleDefault module) {
    Module previous = ACTIVE.getAndSet(module);
    if (previous != null && module != null) {
      LOG.warn("Current module has been changed");
    }
  }
}
