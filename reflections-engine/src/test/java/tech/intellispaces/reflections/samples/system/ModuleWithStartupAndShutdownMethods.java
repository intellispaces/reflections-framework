package tech.intellispaces.reflections.samples.system;

import tech.intellispaces.reflections.annotation.Module;
import tech.intellispaces.reflections.annotation.Shutdown;
import tech.intellispaces.reflections.annotation.Startup;

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
