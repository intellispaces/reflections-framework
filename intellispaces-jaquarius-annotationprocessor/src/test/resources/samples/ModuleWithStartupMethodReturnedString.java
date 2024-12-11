package samples;

import tech.intellispaces.jaquarius.annotation.Module;
import tech.intellispaces.jaquarius.annotation.Startup;
import tech.intellispaces.jaquarius.annotationprocessor.Sample;

@Sample
@Module
public class ModuleWithStartupMethodReturnedString {

  @Startup
  public String startup() {
    return "";
  }
}
