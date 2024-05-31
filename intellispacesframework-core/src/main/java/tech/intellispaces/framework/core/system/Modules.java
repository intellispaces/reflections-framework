package tech.intellispaces.framework.core.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;

import java.util.concurrent.atomic.AtomicReference;

public class Modules {
  private static final AtomicReference<ModuleDefault> ACTIVE = new AtomicReference<>();
  private static final Logger LOG = LoggerFactory.getLogger(Modules.class);

  public static ModuleDefault activeModule() {
    ModuleDefault module = activeModuleSilently();
    if (module == null) {
      throw UnexpectedViolationException.withMessage("Application active module is not defined. It is possible that the module is not loaded yet");
    }
    return module;
  }

  static ModuleDefault activeModuleSilently() {
    return ACTIVE.get();
  }

  static void setActiveModule(ModuleDefault module) {
    Module previous = ACTIVE.getAndSet(module);
    if (previous != null && module != null) {
      LOG.warn("Application active module has been changed");
    }
  }

  private Modules() {}
}
