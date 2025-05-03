package samples;

import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.Shutdown;
import tech.intellispaces.reflectionsj.annotationprocessor.Sample;

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
