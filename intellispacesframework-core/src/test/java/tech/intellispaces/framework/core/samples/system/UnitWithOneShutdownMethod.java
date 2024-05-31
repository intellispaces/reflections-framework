package tech.intellispaces.framework.core.samples.system;

import tech.intellispaces.framework.core.annotation.Shutdown;
import tech.intellispaces.framework.core.annotation.Unit;

@Unit
public class UnitWithOneShutdownMethod {

  @Shutdown
  public void shutdown() {

  }
}
