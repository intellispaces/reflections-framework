package tech.intellispacesframework.core.samples.system;

import tech.intellispacesframework.core.annotation.Module;
import tech.intellispacesframework.core.annotation.Projection;

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
