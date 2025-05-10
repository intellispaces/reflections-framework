package samples;

import tech.intellispaces.reflectionsframework.annotation.Module;
import tech.intellispaces.reflectionsframework.annotation.Projection;
import tech.intellispaces.reflectionsframework.annotation.Properties;
import tech.intellispaces.reflectionsframework.annotation.validator.Sample;

@Sample
@Module
public abstract class ModuleWithAbstractProjectionAndSingleProjectionDefinitionAnnotationAndParameter {

  @Projection
  @Properties
  public abstract String projection(String value);
}
