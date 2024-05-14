package tech.intellispacesframework.core.samples.system;

import tech.intellispacesframework.core.annotation.Shutdown;
import tech.intellispacesframework.core.annotation.Unit;

@Unit
public class UnitWithOneShutdownMethod {

  @Shutdown
  public void shutdown() {

  }
}
