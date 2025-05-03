package samples;

import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.Startup;
import tech.intellispaces.reflectionsj.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithStartupMethodReturnedString {

  @Startup
  public String startup() {
    return "";
  }
}
