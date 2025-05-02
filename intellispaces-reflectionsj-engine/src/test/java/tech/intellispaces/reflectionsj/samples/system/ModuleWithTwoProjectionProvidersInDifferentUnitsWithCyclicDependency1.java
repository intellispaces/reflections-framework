package tech.intellispaces.reflectionsj.samples.system;

import tech.intellispaces.reflectionsj.annotation.Configuration;
import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.Projection;

public class ModuleWithTwoProjectionProvidersInDifferentUnitsWithCyclicDependency1 {

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
    public String projection2(String projection1) {
      return "projection2";
    }
  }
}
