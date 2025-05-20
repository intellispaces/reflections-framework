package tech.intellispaces.reflections.framework.testsamples.system;

import tech.intellispaces.reflections.framework.annotation.Module;
import tech.intellispaces.reflections.framework.annotation.Shutdown;
import tech.intellispaces.reflections.framework.annotation.Startup;

@Module
public class ModuleWithStartupAndShutdownMethods {

  @Startup
  public void startup() {
    // do nothing
  }

  @Shutdown
  public void shutdown() {
    // do nothing
  }
}
