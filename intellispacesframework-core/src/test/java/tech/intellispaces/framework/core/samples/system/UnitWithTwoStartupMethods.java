package tech.intellispaces.framework.core.samples.system;

import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.Startup;

@Module
public class UnitWithTwoStartupMethods {

  @Startup
  public void startup1() {

  }

  @Startup
  public void startup2() {

  }
}
