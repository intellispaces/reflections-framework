package tech.intellispaces.reflectionsj.samples.system;

import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.Projection;

@Module
public class ModuleWithProjectionProvidersWithSelfCyclicDependency {

  @Projection(lazy = false)
  public String projection1(String projection1) {
    return "projection1";
  }
}
