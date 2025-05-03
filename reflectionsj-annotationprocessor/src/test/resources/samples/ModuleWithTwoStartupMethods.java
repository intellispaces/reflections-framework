package samples;

import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.Startup;
import tech.intellispaces.reflectionsj.annotationprocessor.Sample;

@Sample
@Module
public class ModuleWithTwoStartupMethods {

  @Startup
  public void startup1() {}

  @Startup
  public void startup2() {}
}
