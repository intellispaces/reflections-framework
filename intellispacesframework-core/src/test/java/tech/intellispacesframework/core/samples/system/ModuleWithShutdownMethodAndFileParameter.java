package tech.intellispacesframework.core.samples.system;

import tech.intellispacesframework.core.annotation.Module;
import tech.intellispacesframework.core.annotation.Shutdown;

import java.io.File;

@Module
public class ModuleWithShutdownMethodAndFileParameter {

  @Shutdown
  public void shutdown(File value) {
  }
}
