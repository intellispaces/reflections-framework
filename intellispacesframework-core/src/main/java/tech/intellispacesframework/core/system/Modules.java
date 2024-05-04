package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;

public class Modules {
  private static Module CURRENT;

  public static Module currentModule() {
    if (CURRENT == null) {
      throw UnexpectedViolationException.withMessage("Current system module is not defined. It is possible that the system is not loaded yet");
    }
    return CURRENT;
  }

  private Modules() {}
}
