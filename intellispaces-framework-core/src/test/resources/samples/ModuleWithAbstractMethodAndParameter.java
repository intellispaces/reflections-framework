package samples;

import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.validator.Sample;

@Sample
@Module
public abstract class ModuleWithAbstractMethodAndParameter {

  public abstract String projection(String value);
}
