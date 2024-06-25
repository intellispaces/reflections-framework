package samples;

import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.validator.Sample;

@Sample
@Module
public abstract class ModuleWithAbstractMethodAndParameter {

  public abstract String projection(String value);
}
