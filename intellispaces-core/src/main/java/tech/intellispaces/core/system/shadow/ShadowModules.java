package tech.intellispaces.core.system.shadow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.core.system.Module;
import tech.intellispaces.core.system.Modules;

import java.util.concurrent.atomic.AtomicReference;

public final class ShadowModules {
  private static final AtomicReference<ShadowModule> CURRENT = new AtomicReference<>();
  private static final Logger LOG = LoggerFactory.getLogger(Modules.class);

  private ShadowModules() {}

  public static ShadowModule current() {
    ShadowModule module = currentSilently();
    if (module == null) {
      throw UnexpectedViolationException.withMessage("Current module is not defined. " +
          "It is possible that the module is not loaded yet");
    }
    return module;
  }

  public static ShadowModule currentSilently() {
    return CURRENT.get();
  }

  public static void setCurrentModule(ShadowModule module) {
    Module previous = CURRENT.getAndSet(module);
    if (previous != null && module != null) {
      LOG.warn("Current module has been changed");
    }
  }
}
