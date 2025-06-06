package tech.intellispaces.reflections.framework.testsamples.system;

import tech.intellispaces.reflections.framework.annotation.Configuration;
import tech.intellispaces.reflections.framework.annotation.Module;
import tech.intellispaces.reflections.framework.annotation.Projection;

public class ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency2 {

  @Module(UnitSample.class)
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
