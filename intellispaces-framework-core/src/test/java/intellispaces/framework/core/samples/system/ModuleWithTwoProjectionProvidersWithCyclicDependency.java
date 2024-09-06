package intellispaces.framework.core.samples.system;

import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.Projection;

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
