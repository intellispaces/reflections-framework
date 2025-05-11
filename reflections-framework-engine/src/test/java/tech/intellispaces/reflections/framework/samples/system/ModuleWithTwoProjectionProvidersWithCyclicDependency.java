package tech.intellispaces.reflections.framework.samples.system;

import tech.intellispaces.reflections.framework.annotation.Module;
import tech.intellispaces.reflections.framework.annotation.Projection;

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
