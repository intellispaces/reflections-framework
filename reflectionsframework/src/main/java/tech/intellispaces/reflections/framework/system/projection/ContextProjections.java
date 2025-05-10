package tech.intellispaces.reflections.framework.system.projection;

import tech.intellispaces.reflections.framework.system.Module;
import tech.intellispaces.reflections.framework.system.Modules;

public interface ContextProjections {

  static <T> void add(String name, Class<T> type, T target) {
    Module module = Modules.current();
    module.addContextProjection(name, type, target);
  }

  static void remove(String name) {
    Module module = Modules.currentSilently();
    if (module == null) {
      return;
    }
    module.removeContextProjection(name);
  }
}
