package tech.intellispacesframework.core.test.sample.system;

import tech.intellispacesframework.core.annotation.Startup;

public class UnitWithTwoStartupMethods {

  @Startup
  public void startup1() {

  }

  @Startup
  public void startup2() {

  }
}
