package samples;

import tech.intellispaces.jaquarius.annotation.Module;
import tech.intellispaces.jaquarius.annotation.Shutdown;
import tech.intellispaces.jaquarius.annotationprocessor.Sample;

@Sample
@Module
public class ModuleWithShutdownMethodAndStringParameter {

  @Shutdown
  public void shutdown(String value) {
  }
}
