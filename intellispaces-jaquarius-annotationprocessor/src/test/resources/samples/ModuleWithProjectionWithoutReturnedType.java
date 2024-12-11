package samples;

import tech.intellispaces.jaquarius.annotation.Module;
import tech.intellispaces.jaquarius.annotation.Projection;
import tech.intellispaces.jaquarius.annotationprocessor.Sample;

@Sample
@Module
public class ModuleWithProjectionWithoutReturnedType {

  @Projection
  void projection() {
  }
}
