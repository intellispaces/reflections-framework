package samples;

import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.Shutdown;
import intellispaces.framework.core.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithShutdownMethodReturnedString {

  @Shutdown
  public String shutdown() {
    return "";
  }
}
