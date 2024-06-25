package samples;

import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.Startup;
import tech.intellispaces.framework.core.annotation.Unit;
import tech.intellispaces.framework.core.annotation.validator.Sample;

public interface UnitWithOneStartupMethod {

  @Sample
  @Module(units = IncludedUnit.class)
  public class MainUnit {
  }

  @Unit
  public class IncludedUnit {
    @Startup
    public void startup() {
    }
  }
}