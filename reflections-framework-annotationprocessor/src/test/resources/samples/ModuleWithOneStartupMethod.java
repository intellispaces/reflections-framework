package samples;

import tech.intellispaces.reflections.framework.annotation.Module;
import tech.intellispaces.reflections.framework.annotation.Startup;
import tech.intellispaces.reflections.framework.annotationprocessor.Sample;

@Sample
@Module
public class ModuleWithOneStartupMethod {

  @Startup
  public void startup() {
  }
}
