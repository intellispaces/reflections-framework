package tech.intellispacesframework.core.test.sample.system;

import tech.intellispacesframework.core.annotation.Shutdown;

public class UnitWithShutdownMethodReturnedString {

  @Shutdown
  public String shutdown() {
    return "";
  }
}
