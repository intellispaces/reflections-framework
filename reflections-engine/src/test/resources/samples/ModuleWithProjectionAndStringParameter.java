package samples;

import tech.intellispaces.reflections.annotation.Module;
import tech.intellispaces.reflections.annotation.Projection;
import tech.intellispaces.reflections.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithProjectionAndStringParameter {

  @Projection
  public String projection(String value) {
    return "";
  }
}
