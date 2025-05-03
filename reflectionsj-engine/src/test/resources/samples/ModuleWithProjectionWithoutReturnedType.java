package samples;

import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.Projection;
import tech.intellispaces.reflectionsj.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithProjectionWithoutReturnedType {

  @Projection
  void projection() {
  }
}
