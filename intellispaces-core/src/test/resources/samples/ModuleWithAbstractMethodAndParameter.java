package samples;

import tech.intellispaces.core.annotation.Module;
import tech.intellispaces.core.annotation.validator.Sample;

@Sample
@Module
public abstract class ModuleWithAbstractMethodAndParameter {

  public abstract String projection(String value);
}
