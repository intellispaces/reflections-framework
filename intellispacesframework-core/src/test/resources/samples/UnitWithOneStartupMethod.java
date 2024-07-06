package samples;

import tech.intellispaces.framework.core.annotation.Configuration;
import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.Startup;
import tech.intellispaces.framework.core.annotation.validator.Sample;

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