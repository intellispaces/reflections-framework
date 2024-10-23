package samples;

import intellispaces.framework.core.annotation.Configuration;
import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.Shutdown;
import intellispaces.framework.core.annotation.validator.Sample;

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