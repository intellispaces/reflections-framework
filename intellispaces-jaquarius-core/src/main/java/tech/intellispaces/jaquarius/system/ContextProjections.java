package tech.intellispaces.jaquarius.system;

import tech.intellispaces.jaquarius.system.kernel.KernelFunctions;
import tech.intellispaces.jaquarius.system.kernel.KernelModule;

public interface ContextProjections {

  static <T> void addProjection(String name, Class<T> type, T target) {
    KernelModule module = KernelFunctions.currentModuleSilently();
    if (module == null) {
      return;
    }
    module.projectionRegistry().addContextProjection(name, type, target);
  }

  static void removeProjection(String name) {
    KernelModule module = KernelFunctions.currentModuleSilently();
    if (module == null) {
      return;
    }
    module.projectionRegistry().removeContextProjection(name);
  }
}
