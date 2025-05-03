package samples;

import tech.intellispaces.reflectionsj.annotation.Configuration;
import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.Shutdown;
import tech.intellispaces.reflectionsj.annotation.validator.Sample;

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