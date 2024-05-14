package tech.intellispacesframework.core.samples.system;

import tech.intellispacesframework.core.annotation.Module;
import tech.intellispacesframework.core.annotation.Projection;

@Module
public class ModuleWithProjectionProvidersWithSelfCyclicDependency {

  @Projection(lazy = false)
  public String projection1(String projection1) {
    return "projection1";
  }
}
