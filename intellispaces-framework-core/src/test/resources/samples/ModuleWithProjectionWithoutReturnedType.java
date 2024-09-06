package samples;

import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.Projection;
import intellispaces.framework.core.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithProjectionWithoutReturnedType {

  @Projection
  void projection() {
  }
}
