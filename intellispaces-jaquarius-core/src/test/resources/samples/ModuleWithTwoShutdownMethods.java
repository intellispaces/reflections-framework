package samples;

import intellispaces.jaquarius.annotation.Module;
import intellispaces.jaquarius.annotation.Shutdown;
import intellispaces.jaquarius.annotation.validator.Sample;

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
