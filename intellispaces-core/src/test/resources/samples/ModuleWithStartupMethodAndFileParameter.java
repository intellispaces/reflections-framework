package samples;

import intellispaces.core.annotation.Module;
import intellispaces.core.annotation.Startup;
import intellispaces.core.annotation.validator.Sample;

import java.io.File;

@Sample
@Module
public class ModuleWithStartupMethodAndFileParameter {

  @Startup
  public void startup(File value) {
  }
}
