package samples;

import tech.intellispaces.reflections.annotation.Module;
import tech.intellispaces.reflections.annotation.Shutdown;
import tech.intellispaces.reflections.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithTwoShutdownMethods {

  @Shutdown
  public void shutdown1() {
  }

  @Shutdown
  public void shutdown2() {
  }
}
