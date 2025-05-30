package samples;

import tech.intellispaces.reflections.framework.annotation.Module;
import tech.intellispaces.reflections.framework.annotation.Projection;
import tech.intellispaces.reflections.framework.annotation.Properties;
import tech.intellispaces.reflections.framework.annotationprocessor.Sample;

@Sample
@Module
public abstract class ModuleWithAbstractProjectionAndSingleProjectionDefinitionAnnotationAndParameter {

  @Projection
  @Properties
  public abstract String projection(String value);
}
