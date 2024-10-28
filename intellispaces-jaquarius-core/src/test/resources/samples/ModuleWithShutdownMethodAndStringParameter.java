package samples;

import intellispaces.jaquarius.annotation.Module;
import intellispaces.jaquarius.annotation.Shutdown;
import intellispaces.jaquarius.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithShutdownMethodAndStringParameter {

  @Shutdown
  public void shutdown(String value) {
  }
}
