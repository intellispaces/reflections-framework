package tech.intellispacesframework.core.test.samples.system;

import tech.intellispacesframework.core.annotation.Module;
import tech.intellispacesframework.core.annotation.Shutdown;

@Module
public class ModuleWithOneShutdownMethod {

  @Shutdown
  public void shutdown() {

  }
}
