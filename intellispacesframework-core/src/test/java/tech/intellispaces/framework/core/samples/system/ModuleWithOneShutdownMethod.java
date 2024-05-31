package tech.intellispaces.framework.core.samples.system;

import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.Shutdown;

@Module
public class ModuleWithOneShutdownMethod {

  @Shutdown
  public void shutdown() {

  }
}
