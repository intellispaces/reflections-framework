package tech.intellispacesframework.core.test.samples.system;

import tech.intellispacesframework.core.annotation.Shutdown;

public class UnitWithShutdownMethodReturnedString {

  @Shutdown
  public String shutdown() {
    return "";
  }
}
