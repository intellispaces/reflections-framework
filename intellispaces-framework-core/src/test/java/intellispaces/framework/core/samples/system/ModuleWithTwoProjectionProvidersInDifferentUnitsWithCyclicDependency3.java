package intellispaces.framework.core.samples.system;

import intellispaces.framework.core.annotation.Configuration;
import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.Projection;

public class ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency3 {

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
    public String projection2(String projection3) {
      return "projection2";
    }
  }
}
