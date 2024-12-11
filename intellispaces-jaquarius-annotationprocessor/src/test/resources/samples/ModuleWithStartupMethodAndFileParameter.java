package samples;

import tech.intellispaces.jaquarius.annotation.Module;
import tech.intellispaces.jaquarius.annotation.Startup;
import tech.intellispaces.jaquarius.annotationprocessor.Sample;

import java.io.File;

@Sample
@Module
public class ModuleWithStartupMethodAndFileParameter {

  @Startup
  public void startup(File value) {
  }
}
