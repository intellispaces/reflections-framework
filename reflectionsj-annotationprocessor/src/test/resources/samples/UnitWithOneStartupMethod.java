package samples;

import tech.intellispaces.reflectionsj.annotation.Configuration;
import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.Startup;
import tech.intellispaces.reflectionsj.annotationprocessor.Sample;

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