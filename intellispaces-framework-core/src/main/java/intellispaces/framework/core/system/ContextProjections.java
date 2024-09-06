package intellispaces.framework.core.system;

import intellispaces.framework.core.system.kernel.KernelModule;
import intellispaces.framework.core.system.kernel.KernelModules;

public interface ContextProjections {

  static <T> void addProjection(String name, Class<T> type, T target) {
    KernelModule module = KernelModules.currentSilently();
    if (module == null) {
      return;
    }
    module.projectionRegistry().addContextProjection(name, type, target);
  }

  static void removeProjection(String name) {
    KernelModule module = KernelModules.currentSilently();
    if (module == null) {
      return;
    }
    module.projectionRegistry().removeContextProjection(name);
  }
}
