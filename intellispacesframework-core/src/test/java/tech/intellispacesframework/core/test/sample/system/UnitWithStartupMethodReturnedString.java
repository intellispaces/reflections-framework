package tech.intellispacesframework.core.test.sample.system;

import tech.intellispacesframework.core.annotation.Startup;

public class UnitWithStartupMethodReturnedString {

  @Startup
  public String startup() {
    return "";
  }
}
