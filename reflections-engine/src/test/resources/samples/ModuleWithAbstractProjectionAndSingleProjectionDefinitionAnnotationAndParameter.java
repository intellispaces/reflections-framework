package samples;

import tech.intellispaces.reflections.annotation.Module;
import tech.intellispaces.reflections.annotation.Projection;
import tech.intellispaces.reflections.annotation.Properties;
import tech.intellispaces.reflections.annotation.validator.Sample;

@Sample
@Module
public abstract class ModuleWithAbstractProjectionAndSingleProjectionDefinitionAnnotationAndParameter {

  @Projection
  @Properties
  public abstract String projection(String value);
}
