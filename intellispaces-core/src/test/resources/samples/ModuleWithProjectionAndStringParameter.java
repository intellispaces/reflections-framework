package samples;

import tech.intellispaces.core.annotation.Module;
import tech.intellispaces.core.annotation.Projection;
import tech.intellispaces.core.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithProjectionAndStringParameter {

  @Projection
  public String projection(String value) {
    return "";
  }
}
