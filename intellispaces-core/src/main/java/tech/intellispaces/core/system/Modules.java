package tech.intellispaces.core.system;

import tech.intellispaces.commons.exception.UnexpectedViolationException;

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
    return DefaultModules.currentSilently();
  }
}
