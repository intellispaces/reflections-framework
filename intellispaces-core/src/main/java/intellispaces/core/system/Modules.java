package intellispaces.core.system;

import intellispaces.commons.exception.UnexpectedViolationException;
import intellispaces.core.system.shadow.ShadowModules;

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
    return ShadowModules.currentSilently();
  }
}
