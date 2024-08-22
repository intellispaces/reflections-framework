package samples;

import intellispaces.core.annotation.Module;
import intellispaces.core.annotation.validator.Sample;

@Sample
@Module
public abstract class ModuleWithAbstractMethodAndParameter {

  public abstract String projection(String value);
}
