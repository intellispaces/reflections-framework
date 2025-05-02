package samples;

import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.Projection;
import tech.intellispaces.reflectionsj.annotation.Properties;
import tech.intellispaces.reflectionsj.annotation.validator.Sample;

@Sample
@Module
public abstract class ModuleWithAbstractProjectionAndSingleProjectionDefinitionAnnotationAndParameter {

  @Projection
  @Properties
  public abstract String projection(String value);
}
