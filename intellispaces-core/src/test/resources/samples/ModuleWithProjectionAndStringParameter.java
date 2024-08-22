package samples;

import intellispaces.core.annotation.Module;
import intellispaces.core.annotation.Projection;
import intellispaces.core.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithProjectionAndStringParameter {

  @Projection
  public String projection(String value) {
    return "";
  }
}
