package samples;

import intellispaces.core.annotation.Module;
import intellispaces.core.annotation.Startup;
import intellispaces.core.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithTwoStartupMethods {

  @Startup
  public void startup1() {}

  @Startup
  public void startup2() {}
}
