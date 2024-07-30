package samples;

import tech.intellispaces.core.annotation.Module;
import tech.intellispaces.core.annotation.Projection;
import tech.intellispaces.core.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithProjectionWithoutReturnedType {

  @Projection
  void projection() {
  }
}
