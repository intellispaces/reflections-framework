package tech.intellispacesframework.core.test.samples.system;

import tech.intellispacesframework.core.annotation.Startup;

public class UnitWithTwoStartupMethods {

  @Startup
  public void startup1() {

  }

  @Startup
  public void startup2() {

  }
}
