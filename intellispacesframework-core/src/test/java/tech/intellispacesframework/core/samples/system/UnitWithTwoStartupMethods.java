package tech.intellispacesframework.core.samples.system;

import tech.intellispacesframework.core.annotation.Module;
import tech.intellispacesframework.core.annotation.Startup;

@Module
public class UnitWithTwoStartupMethods {

  @Startup
  public void startup1() {

  }

  @Startup
  public void startup2() {

  }
}
