package samples;

import intellispaces.core.annotation.Module;
import intellispaces.core.annotation.Projection;
import intellispaces.core.annotation.Properties;
import intellispaces.core.annotation.validator.Sample;

@Sample
@Module
public abstract class ModuleWithAbstractProjectionAndSingleProjectionDefinitionAnnotationAndParameter {

  @Projection
  @Properties
  public abstract String projection(String value);
}
