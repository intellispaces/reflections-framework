package samples;

import tech.intellispaces.reflectionsframework.annotation.Module;
import tech.intellispaces.reflectionsframework.annotation.Startup;
import tech.intellispaces.reflectionsframework.annotationprocessor.Sample;

@Sample
@Module
public class ModuleWithStartupMethodReturnedString {

  @Startup
  public String startup() {
    return "";
  }
}
