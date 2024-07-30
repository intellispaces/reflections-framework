package samples;

import tech.intellispaces.core.annotation.Module;
import tech.intellispaces.core.annotation.Shutdown;
import tech.intellispaces.core.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithShutdownMethodAndStringParameter {

  @Shutdown
  public void shutdown(String value) {
  }
}
