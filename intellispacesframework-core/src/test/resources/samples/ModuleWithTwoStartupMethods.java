package samples;

import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.Startup;
import tech.intellispaces.framework.core.annotation.validator.Sample;

@Sample
@Module
public class ModuleWithTwoStartupMethods {

  @Startup
  public void startup1() {}

  @Startup
  public void startup2() {}
}
