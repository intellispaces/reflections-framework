package tech.intellispaces.framework.core.samples.system;

import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.Shutdown;

@Module
public class ModuleWithTwoShutdownMethods {

  @Shutdown
  public void shutdown1() {

  }

  @Shutdown
  public void shutdown2() {

  }
}
