package samples;

import tech.intellispaces.reflectionsframework.annotation.Module;
import tech.intellispaces.reflectionsframework.annotation.Shutdown;
import tech.intellispaces.reflectionsframework.annotationprocessor.Sample;

@Sample
@Module
public class ModuleWithShutdownMethodAndStringParameter {

  @Shutdown
  public void shutdown(String value) {
  }
}
