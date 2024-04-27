package tech.intellispacesframework.core.test.sample.system;

import tech.intellispacesframework.core.annotation.Shutdown;

public class UnitWithTwoShutdownMethods {

  @Shutdown
  public void shutdown1() {

  }

  @Shutdown
  public void shutdown2() {

  }
}
