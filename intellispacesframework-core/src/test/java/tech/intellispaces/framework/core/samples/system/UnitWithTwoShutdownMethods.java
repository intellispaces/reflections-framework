package tech.intellispaces.framework.core.samples.system;

import tech.intellispaces.framework.core.annotation.Shutdown;
import tech.intellispaces.framework.core.annotation.Unit;

@Unit
public class UnitWithTwoShutdownMethods {

  @Shutdown
  public void shutdown1() {

  }

  @Shutdown
  public void shutdown2() {

  }
}
