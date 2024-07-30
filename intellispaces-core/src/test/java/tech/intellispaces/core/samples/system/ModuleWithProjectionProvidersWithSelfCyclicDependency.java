package tech.intellispaces.core.samples.system;

import tech.intellispaces.core.annotation.Module;
import tech.intellispaces.core.annotation.Projection;

@Module
public class ModuleWithProjectionProvidersWithSelfCyclicDependency {

  @Projection(lazy = false)
  public String projection1(String projection1) {
    return "projection1";
  }
}
