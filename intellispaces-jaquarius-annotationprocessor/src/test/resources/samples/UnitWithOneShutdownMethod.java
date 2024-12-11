package samples;

import tech.intellispaces.jaquarius.annotation.Configuration;
import tech.intellispaces.jaquarius.annotation.Module;
import tech.intellispaces.jaquarius.annotation.Shutdown;
import tech.intellispaces.jaquarius.annotationprocessor.Sample;

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