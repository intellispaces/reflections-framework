package samples;

import intellispaces.jaquarius.annotation.Configuration;
import intellispaces.jaquarius.annotation.Module;
import intellispaces.jaquarius.annotation.Shutdown;
import intellispaces.jaquarius.annotation.validator.Sample;

public interface UnitWithOneShutdownMethod {

  @Sample
  @Module(IncludedUnit.class)
  public class MainUnit {
  }

  @Configuration
  public class IncludedUnit {
    @Shutdown
    public void shutdown() {
    }
  }
}