package samples;

import tech.intellispaces.jaquarius.annotation.Configuration;
import tech.intellispaces.jaquarius.annotation.Module;
import tech.intellispaces.jaquarius.annotation.Startup;
import tech.intellispaces.jaquarius.annotationprocessor.Sample;

public interface UnitWithOneStartupMethod {

  @Sample
  @Module(IncludedUnit.class)
  public class MainUnit {
  }

  @Configuration
  public class IncludedUnit {
    @Startup
    public void startup() {
    }
  }
}