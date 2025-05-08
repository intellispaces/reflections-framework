package samples;

import tech.intellispaces.reflections.annotation.Module;
import tech.intellispaces.reflections.annotation.Startup;
import tech.intellispaces.reflections.annotationprocessor.Sample;

@Sample
@Module
public class ModuleWithOneStartupMethod {

  @Startup
  public void startup() {
  }
}
