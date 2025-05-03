package samples;

import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.Startup;
import tech.intellispaces.reflectionsj.annotationprocessor.Sample;

@Sample
@Module
public class ModuleWithOneStartupMethod {

  @Startup
  public void startup() {
  }
}
