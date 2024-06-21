package samples;

import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.Shutdown;
import tech.intellispaces.framework.core.annotation.Unit;
import tech.intellispaces.framework.core.validate.Sample;

public interface UnitWithOneShutdownMethod {

  @Sample
  @Module(units = IncludedUnit.class)
  public class MainUnit {
  }

  @Unit
  public class IncludedUnit {
    @Shutdown
    public void shutdown() {
    }
  }
}