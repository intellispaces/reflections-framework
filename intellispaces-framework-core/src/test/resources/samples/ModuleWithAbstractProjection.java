package samples;

import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.Projection;
import intellispaces.framework.core.annotation.validator.Sample;

@Sample
@Module
public abstract class ModuleWithAbstractProjection {

  @Projection
  public abstract String projection();
}
