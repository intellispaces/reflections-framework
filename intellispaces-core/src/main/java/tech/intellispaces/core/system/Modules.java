package tech.intellispaces.core.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.intellispaces.commons.exception.UnexpectedViolationException;

import java.util.concurrent.atomic.AtomicReference;

public class Modules {
  private static final AtomicReference<Module> CURRENT = new AtomicReference<>();
  private static final Logger LOG = LoggerFactory.getLogger(Modules.class);

  private Modules() {}

  public static Module current() {
    Module module = currentSilently();
    if (module == null) {
      throw UnexpectedViolationException.withMessage("Current module is not defined. " +
          "It is possible that the module is not loaded yet");
    }
    return module;
  }

  static Module currentSilently() {
    return CURRENT.get();
  }

  static void setCurrentModule(Module module) {
    Module previous = CURRENT.getAndSet(module);
    if (previous != null && module != null) {
      LOG.warn("Current module has been changed");
    }
  }
}
