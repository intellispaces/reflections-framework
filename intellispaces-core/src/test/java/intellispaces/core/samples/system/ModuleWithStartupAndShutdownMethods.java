package intellispaces.core.samples.system;

import intellispaces.core.annotation.Module;
import intellispaces.core.annotation.Shutdown;
import intellispaces.core.annotation.Startup;

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
