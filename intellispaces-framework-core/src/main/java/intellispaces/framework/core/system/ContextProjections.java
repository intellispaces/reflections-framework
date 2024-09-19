package intellispaces.framework.core.system;

import intellispaces.framework.core.system.kernel.SystemModule;
import intellispaces.framework.core.system.kernel.SystemFunctions;

public interface ContextProjections {

  static <T> void addProjection(String name, Class<T> type, T target) {
    SystemModule module = SystemFunctions.currentModuleSilently();
    if (module == null) {
      return;
    }
    module.projectionRegistry().addContextProjection(name, type, target);
  }

  static void removeProjection(String name) {
    SystemModule module = SystemFunctions.currentModuleSilently();
    if (module == null) {
      return;
    }
    module.projectionRegistry().removeContextProjection(name);
  }
}
