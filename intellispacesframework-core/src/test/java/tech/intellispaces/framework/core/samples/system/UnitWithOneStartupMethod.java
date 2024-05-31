package tech.intellispaces.framework.core.samples.system;

import tech.intellispaces.framework.core.annotation.Startup;
import tech.intellispaces.framework.core.annotation.Unit;

@Unit
public class UnitWithOneStartupMethod {

  @Startup
  public void startup() {

  }
}
