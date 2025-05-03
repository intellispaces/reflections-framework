package samples;

import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.Shutdown;
import tech.intellispaces.reflectionsj.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithShutdownMethodAndStringParameter {

  @Shutdown
  public void shutdown(String value) {
  }
}
