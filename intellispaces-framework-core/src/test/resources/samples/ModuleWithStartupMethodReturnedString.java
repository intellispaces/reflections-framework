package samples;

import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.Startup;
import intellispaces.framework.core.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithStartupMethodReturnedString {

  @Startup
  public String startup() {
    return "";
  }
}
