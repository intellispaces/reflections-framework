package samples;

import tech.intellispaces.reflections.annotation.Module;
import tech.intellispaces.reflections.annotation.Startup;
import tech.intellispaces.reflections.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithTwoStartupMethods {

  @Startup
  public void startup1() {}

  @Startup
  public void startup2() {}
}
