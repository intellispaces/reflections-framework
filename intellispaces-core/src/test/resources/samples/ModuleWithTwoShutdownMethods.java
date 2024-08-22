package samples;

import intellispaces.core.annotation.Module;
import intellispaces.core.annotation.Shutdown;
import intellispaces.core.annotation.validator.Sample;

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
