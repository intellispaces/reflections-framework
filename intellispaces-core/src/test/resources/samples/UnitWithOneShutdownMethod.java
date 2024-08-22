package samples;

import intellispaces.core.annotation.Configuration;
import intellispaces.core.annotation.Module;
import intellispaces.core.annotation.Shutdown;
import intellispaces.core.annotation.validator.Sample;

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