package samples;

import tech.intellispaces.reflections.annotation.Module;
import tech.intellispaces.reflections.annotationprocessor.Sample;

@Sample
@Module
public abstract class ModuleWithAbstractMethodAndParameter {

  public abstract String projection(String value);
}
