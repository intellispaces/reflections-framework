package tech.intellispacesframework.core.test.samples.system;

import tech.intellispacesframework.core.annotation.Shutdown;

public class UnitWithShutdownMethodAndStringParameter {

  @Shutdown
  public void shutdown(String value) {
  }
}
