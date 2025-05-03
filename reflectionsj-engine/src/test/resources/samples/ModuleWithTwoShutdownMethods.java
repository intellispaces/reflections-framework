package samples;

import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.Shutdown;
import tech.intellispaces.reflectionsj.annotation.validator.Sample;

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
