package samples;

import java.io.File;

import tech.intellispaces.reflections.annotation.Module;
import tech.intellispaces.reflections.annotation.Startup;
import tech.intellispaces.reflections.annotationprocessor.Sample;

@Sample
@Module
public class ModuleWithStartupMethodAndFileParameter {

  @Startup
  public void startup(File value) {
  }
}
