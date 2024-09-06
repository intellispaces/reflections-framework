package samples;

import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.Startup;
import intellispaces.framework.core.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithStartupMethodAndStringParameter {

  @Startup
  public void startup(String value) {
  }
}
