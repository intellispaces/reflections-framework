package samples;

import tech.intellispaces.reflections.annotation.Module;
import tech.intellispaces.reflections.annotation.Shutdown;
import tech.intellispaces.reflections.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithShutdownMethodAndStringParameter {

  @Shutdown
  public void shutdown(String value) {
  }
}
