package samples;

import tech.intellispaces.core.annotation.Configuration;
import tech.intellispaces.core.annotation.Module;
import tech.intellispaces.core.annotation.Shutdown;
import tech.intellispaces.core.annotation.validator.Sample;

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