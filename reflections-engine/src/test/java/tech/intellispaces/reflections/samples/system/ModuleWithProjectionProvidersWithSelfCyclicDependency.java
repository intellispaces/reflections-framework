package tech.intellispaces.reflections.samples.system;

import tech.intellispaces.reflections.annotation.Module;
import tech.intellispaces.reflections.annotation.Projection;

@Module
public class ModuleWithProjectionProvidersWithSelfCyclicDependency {

  @Projection(lazy = false)
  public String projection1(String projection1) {
    return "projection1";
  }
}
