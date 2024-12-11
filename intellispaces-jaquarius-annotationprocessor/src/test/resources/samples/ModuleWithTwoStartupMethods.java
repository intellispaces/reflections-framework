package samples;

import tech.intellispaces.jaquarius.annotation.Module;
import tech.intellispaces.jaquarius.annotation.Startup;
import tech.intellispaces.jaquarius.annotationprocessor.Sample;

@Sample
@Module
public class ModuleWithTwoStartupMethods {

  @Startup
  public void startup1() {}

  @Startup
  public void startup2() {}
}
