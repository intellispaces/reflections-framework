package samples;

import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.Shutdown;
import intellispaces.framework.core.annotation.validator.Sample;

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
