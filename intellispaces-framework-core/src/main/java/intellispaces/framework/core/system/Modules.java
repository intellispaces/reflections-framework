package intellispaces.framework.core.system;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.system.kernel.KernelModules;

public interface Modules {

  static Module current() {
    Module module = currentSilently();
    if (module == null) {
      throw UnexpectedViolationException.withMessage("Current module is not defined. " +
          "It is possible that the module is not loaded yet");
    }
    return module;
  }

  static Module currentSilently() {
    return KernelModules.currentSilently();
  }
}
