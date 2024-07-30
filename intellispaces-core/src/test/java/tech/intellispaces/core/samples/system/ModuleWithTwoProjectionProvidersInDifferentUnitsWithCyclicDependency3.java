package tech.intellispaces.core.samples.system;

import tech.intellispaces.core.annotation.Configuration;
import tech.intellispaces.core.annotation.Module;
import tech.intellispaces.core.annotation.Projection;

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
