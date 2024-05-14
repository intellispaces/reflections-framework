package tech.intellispacesframework.core.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.intellispacesframework.commons.exception.UnexpectedViolationException;

import java.util.concurrent.atomic.AtomicReference;

public class Modules {
  private static final AtomicReference<Module> CURRENT = new AtomicReference<>();
  private static final Logger LOG = LoggerFactory.getLogger(Modules.class);

  public static Module activeModule() {
    Module module = CURRENT.get();
    if (module == null) {
      throw UnexpectedViolationException.withMessage("Application active module is not defined. It is possible that the module is not loaded yet");
    }
    return module;
  }

  static void setActiveModule(Module module) {
    Module previous = CURRENT.getAndSet(module);
    if (previous != null) {
      LOG.warn("Application active module has been changed");
    }
  }

  private Modules() {}
}
