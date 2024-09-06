package intellispaces.framework.core.samples.system;

import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.Shutdown;
import intellispaces.framework.core.annotation.Startup;

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
