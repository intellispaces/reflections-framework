package tech.intellispacesframework.core.samples.system;

import tech.intellispacesframework.core.annotation.Startup;
import tech.intellispacesframework.core.annotation.Unit;

@Unit
public class UnitWithOneStartupMethod {

  @Startup
  public void startup() {

  }
}
