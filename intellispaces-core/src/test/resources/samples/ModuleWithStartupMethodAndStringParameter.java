package samples;

import intellispaces.core.annotation.Module;
import intellispaces.core.annotation.Startup;
import intellispaces.core.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithStartupMethodAndStringParameter {

  @Startup
  public void startup(String value) {
  }
}
