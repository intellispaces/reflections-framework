package samples;

import tech.intellispaces.reflections.framework.annotation.Module;
import tech.intellispaces.reflections.framework.annotationprocessor.Sample;

@Sample
@Module
public abstract class ModuleWithAbstractMethodAndParameter {

  public abstract String projection(String value);
}
