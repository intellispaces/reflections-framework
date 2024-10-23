package samples;

import intellispaces.framework.core.annotation.Configuration;
import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.Startup;
import intellispaces.framework.core.annotation.validator.Sample;

public interface UnitWithOneStartupMethod {

  @Sample
  @Module(IncludedUnit.class)
  public class MainUnit {
  }

  @Configuration
  public class IncludedUnit {
    @Startup
    public void startup() {
    }
  }
}