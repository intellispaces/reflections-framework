package intellispaces.core.samples.system;

import intellispaces.core.annotation.Configuration;
import intellispaces.core.annotation.Module;
import intellispaces.core.annotation.Projection;

public class ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency2 {

  @Module(units = UnitSample.class)
  public static class ModuleSample {
    @Projection(lazy = false)
    public String projection1(String projection2) {
      return "projection1";
    }
  }

  @Configuration
  public static class UnitSample {
    @Projection
    public String projection2(String projection2) {
      return "projection2";
    }
  }
}
