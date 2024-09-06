package intellispaces.framework.core.samples.system;

import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.Projection;

@Module
public class ModuleWithProjectionProvidersWithSelfCyclicDependency {

  @Projection(lazy = false)
  public String projection1(String projection1) {
    return "projection1";
  }
}
