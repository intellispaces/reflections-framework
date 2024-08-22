package samples;

import intellispaces.core.annotation.Module;
import intellispaces.core.annotation.Shutdown;
import intellispaces.core.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithOneShutdownMethod {

  @Shutdown
  public void shutdown() {
  }
}
