package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;

public class Modules {
  private static SystemModule CURRENT;

  public static SystemModule current() {
    if (CURRENT == null) {
      throw UnexpectedViolationException.withMessage("Current system module is not defined. It is possible that the system is not loaded yet");
    }
    return CURRENT;
  }

  private static void setCurrent(SystemModule module) {
    CURRENT = module;
  }

  private Modules() {}
}
