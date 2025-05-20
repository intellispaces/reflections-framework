package tech.intellispaces.reflections.framework.system.projection;

import tech.intellispaces.reflections.framework.system.ModuleHandle;
import tech.intellispaces.reflections.framework.system.Modules;

public interface ContextProjections {

  static <T> void add(String name, Class<T> type, T target) {
    ModuleHandle module = Modules.current();
    module.addContextProjection(name, type, target);
  }

  static void remove(String name) {
    ModuleHandle module = Modules.currentSilently();
    if (module == null) {
      return;
    }
    module.removeContextProjection(name);
  }
}
