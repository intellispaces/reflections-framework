package tech.intellispacesframework.core.test.samples.system;

import tech.intellispacesframework.core.annotation.Shutdown;

import java.io.File;

public class UnitWithShutdownMethodAndFileParameter {

  @Shutdown
  public void shutdown(File value) {
  }
}
