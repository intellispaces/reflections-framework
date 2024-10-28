package intellispaces.jaquarius.samples.system;

import intellispaces.jaquarius.annotation.Module;
import intellispaces.jaquarius.annotation.Projection;

@Module
public class ModuleWithProjectionProvidersWithSelfCyclicDependency {

  @Projection(lazy = false)
  public String projection1(String projection1) {
    return "projection1";
  }
}
