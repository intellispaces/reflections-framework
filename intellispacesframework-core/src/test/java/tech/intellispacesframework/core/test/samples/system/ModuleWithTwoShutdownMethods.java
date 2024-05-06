package tech.intellispacesframework.core.test.samples.system;

import tech.intellispacesframework.core.annotation.Module;
import tech.intellispacesframework.core.annotation.Shutdown;

@Module
public class ModuleWithTwoShutdownMethods {

  @Shutdown
  public void shutdown1() {

  }

  @Shutdown
  public void shutdown2() {

  }
}
