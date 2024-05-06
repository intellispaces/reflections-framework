package tech.intellispacesframework.core.test.samples.system;

import tech.intellispacesframework.core.annotation.Module;
import tech.intellispacesframework.core.annotation.Startup;

@Module
public class ModuleWithTwoStartupMethods {

  @Startup
  public void startup1() {

  }

  @Startup
  public void startup2() {

  }
}
