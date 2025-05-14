package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.reflections.framework.exception.ConfigurationExceptions;

import java.util.concurrent.atomic.AtomicReference;

public class Systems {
  private final static AtomicReference<System> SYSTEM = new AtomicReference<>();

  /**
   * Returns the current loaded system.
   * <p>
   * If there is no current system, an exception will be thrown.
   */
  public static System current() {
    System system = SYSTEM.get();
    if (system == null) {
      throw ConfigurationExceptions.withMessage("The current system is not defined");
    }
    return system;
  }
}
