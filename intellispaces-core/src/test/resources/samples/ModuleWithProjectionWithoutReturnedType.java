package samples;

import intellispaces.core.annotation.Module;
import intellispaces.core.annotation.Projection;
import intellispaces.core.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithProjectionWithoutReturnedType {

  @Projection
  void projection() {
  }
}
