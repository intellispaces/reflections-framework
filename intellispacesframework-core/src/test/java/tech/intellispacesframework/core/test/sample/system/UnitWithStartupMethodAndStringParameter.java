package tech.intellispacesframework.core.test.sample.system;

import tech.intellispacesframework.core.annotation.Startup;

public class UnitWithStartupMethodAndStringParameter {

  @Startup
  public void startup(String value) {
  }
}
