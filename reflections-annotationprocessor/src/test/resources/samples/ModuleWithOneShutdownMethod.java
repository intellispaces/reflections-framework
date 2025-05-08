package samples;

import tech.intellispaces.reflections.annotation.Module;
import tech.intellispaces.reflections.annotation.Shutdown;
import tech.intellispaces.reflections.annotationprocessor.Sample;

@Sample
@Module
public class ModuleWithOneShutdownMethod {

  @Shutdown
  public void shutdown() {
  }
}
