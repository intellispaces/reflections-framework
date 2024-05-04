package tech.intellispacesframework.core.test.samples.system;

import tech.intellispacesframework.core.annotation.Startup;

public class UnitWithStartupMethodReturnedString {

  @Startup
  public String startup() {
    return "";
  }
}
