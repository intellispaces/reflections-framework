package intellispaces.framework.core.system;

import intellispaces.framework.core.system.kernel.KernelFunctions;
import intellispaces.framework.core.system.kernel.KernelModule;

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
