package samples;

import intellispaces.core.annotation.Module;
import intellispaces.core.annotation.Shutdown;
import intellispaces.core.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithShutdownMethodAndStringParameter {

  @Shutdown
  public void shutdown(String value) {
  }
}
