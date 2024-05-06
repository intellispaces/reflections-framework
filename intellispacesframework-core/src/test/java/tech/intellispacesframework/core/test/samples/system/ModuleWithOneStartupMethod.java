package tech.intellispacesframework.core.test.samples.system;

import tech.intellispacesframework.core.annotation.Module;
import tech.intellispacesframework.core.annotation.Startup;

@Module
public class ModuleWithOneStartupMethod {

  @Startup
  public void startup() {

  }
}
