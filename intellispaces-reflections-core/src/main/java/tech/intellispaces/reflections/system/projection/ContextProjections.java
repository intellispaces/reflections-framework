package tech.intellispaces.reflections.system.projection;

import tech.intellispaces.reflections.system.Modules;

public interface ContextProjections {

  static <T> void add(String name, Class<T> type, T target) {
    tech.intellispaces.reflections.system.Module module = Modules.current();
    module.addContextProjection(name, type, target);
  }

  static void remove(String name) {
    tech.intellispaces.reflections.system.Module module = Modules.currentSilently();
    if (module == null) {
      return;
    }
    module.removeContextProjection(name);
  }
}
