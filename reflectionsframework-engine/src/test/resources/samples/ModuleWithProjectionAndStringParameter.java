package samples;

import tech.intellispaces.reflectionsframework.annotation.Module;
import tech.intellispaces.reflectionsframework.annotation.Projection;
import tech.intellispaces.reflectionsframework.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithProjectionAndStringParameter {

  @Projection
  public String projection(String value) {
    return "";
  }
}
