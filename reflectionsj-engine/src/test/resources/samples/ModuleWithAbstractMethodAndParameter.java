package samples;

import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.validator.Sample;

@Sample
@Module
public abstract class ModuleWithAbstractMethodAndParameter {

  public abstract String projection(String value);
}
