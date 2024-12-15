package tech.intellispaces.jaquarius.samples.system;

import tech.intellispaces.jaquarius.annotation.Module;
import tech.intellispaces.jaquarius.annotation.Projection;

@Module
public class ModuleWithTwoProjectionProvidersWithCyclicDependency {

  @Projection(lazy = false)
  public String projection1(String projection2) {
    return "projection1";
  }

  @Projection
  public String projection2(String projection1) {
    return "projection2";
  }
}
