package samples;

import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.Projection;
import intellispaces.framework.core.annotation.Properties;
import intellispaces.framework.core.annotation.validator.Sample;

@Sample
@Module
public abstract class ModuleWithAbstractProjectionAndSingleProjectionDefinitionAnnotation {

  @Projection
  @Properties
  public abstract String projection();
}
