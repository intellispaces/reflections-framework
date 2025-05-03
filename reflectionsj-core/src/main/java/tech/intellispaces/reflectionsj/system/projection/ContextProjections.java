package tech.intellispaces.reflectionsj.system.projection;

import tech.intellispaces.reflectionsj.system.Modules;

public interface ContextProjections {

  static <T> void add(String name, Class<T> type, T target) {
    tech.intellispaces.reflectionsj.system.Module module = Modules.current();
    module.addContextProjection(name, type, target);
  }

  static void remove(String name) {
    tech.intellispaces.reflectionsj.system.Module module = Modules.currentSilently();
    if (module == null) {
      return;
    }
    module.removeContextProjection(name);
  }
}
