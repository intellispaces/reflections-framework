package tech.intellispaces.reflectionsj.samples.system;

import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.Shutdown;
import tech.intellispaces.reflectionsj.annotation.Startup;

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
