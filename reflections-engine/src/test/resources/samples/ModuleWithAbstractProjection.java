package samples;

import tech.intellispaces.reflections.annotation.Module;
import tech.intellispaces.reflections.annotation.Projection;
import tech.intellispaces.reflections.annotation.validator.Sample;

@Sample
@Module
public abstract class ModuleWithAbstractProjection {

  @Projection
  public abstract String projection();
}
