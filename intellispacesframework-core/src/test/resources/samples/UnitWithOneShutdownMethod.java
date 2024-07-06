package samples;

import tech.intellispaces.framework.core.annotation.Configuration;
import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.Shutdown;
import tech.intellispaces.framework.core.annotation.validator.Sample;

public interface UnitWithOneShutdownMethod {

  @Sample
  @Module(units = IncludedUnit.class)
  public class MainUnit {
  }

  @Configuration
  public class IncludedUnit {
    @Shutdown
    public void shutdown() {
    }
  }
}