package samples;

import tech.intellispaces.jaquarius.annotation.Module;
import tech.intellispaces.jaquarius.annotation.Shutdown;
import tech.intellispaces.jaquarius.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithShutdownMethodReturnedString {

  @Shutdown
  public String shutdown() {
    return "";
  }
}
