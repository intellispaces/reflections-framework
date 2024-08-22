package samples;

import intellispaces.core.annotation.Configuration;
import intellispaces.core.annotation.Module;
import intellispaces.core.annotation.Startup;
import intellispaces.core.annotation.validator.Sample;

public interface UnitWithOneStartupMethod {

  @Sample
  @Module(units = IncludedUnit.class)
  public class MainUnit {
  }

  @Configuration
  public class IncludedUnit {
    @Startup
    public void startup() {
    }
  }
}