package samples;

import intellispaces.jaquarius.annotation.Module;
import intellispaces.jaquarius.annotation.validator.Sample;

@Sample
@Module
public abstract class ModuleWithAbstractMethodAndParameter {

  public abstract String projection(String value);
}
