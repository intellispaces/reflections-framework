package tech.intellispacesframework.core.test.sample.system;

import tech.intellispacesframework.core.annotation.Shutdown;

public class UnitWithShutdownMethodAndStringParameter {

  @Shutdown
  public void shutdown(String value) {
  }
}
