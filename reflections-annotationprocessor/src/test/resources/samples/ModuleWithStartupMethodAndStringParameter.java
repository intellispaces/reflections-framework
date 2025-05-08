package samples;

import tech.intellispaces.reflections.annotation.Module;
import tech.intellispaces.reflections.annotation.Startup;
import tech.intellispaces.reflections.annotationprocessor.Sample;

@Sample
@Module
public class ModuleWithStartupMethodAndStringParameter {

  @Startup
  public void startup(String value) {
  }
}
