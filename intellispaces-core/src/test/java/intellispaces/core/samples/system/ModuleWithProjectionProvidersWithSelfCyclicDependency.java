package intellispaces.core.samples.system;

import intellispaces.core.annotation.Module;
import intellispaces.core.annotation.Projection;

@Module
public class ModuleWithProjectionProvidersWithSelfCyclicDependency {

  @Projection(lazy = false)
  public String projection1(String projection1) {
    return "projection1";
  }
}
