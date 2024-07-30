package samples;

import tech.intellispaces.core.annotation.Module;
import tech.intellispaces.core.annotation.Startup;
import tech.intellispaces.core.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithTwoStartupMethods {

  @Startup
  public void startup1() {}

  @Startup
  public void startup2() {}
}
