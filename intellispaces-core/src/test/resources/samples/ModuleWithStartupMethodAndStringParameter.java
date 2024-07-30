package samples;

import tech.intellispaces.core.annotation.Module;
import tech.intellispaces.core.annotation.Startup;
import tech.intellispaces.core.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithStartupMethodAndStringParameter {

  @Startup
  public void startup(String value) {
  }
}
