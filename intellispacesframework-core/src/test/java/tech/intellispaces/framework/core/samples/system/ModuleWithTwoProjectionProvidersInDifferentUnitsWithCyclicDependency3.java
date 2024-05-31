package tech.intellispaces.framework.core.samples.system;

import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.Projection;
import tech.intellispaces.framework.core.annotation.Unit;

public class ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency3 {

  @Module(units = UnitSample.class)
  public static class ModuleSample {
    @Projection(lazy = false)
    public String projection1(String projection2) {
      return "projection1";
    }
  }

  @Unit
  public static class UnitSample {
    @Projection
    public String projection2(String projection3) {
      return "projection2";
    }
  }
}
