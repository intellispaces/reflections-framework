package tech.intellispaces.core.system;


public interface ModuleProjections {

  static <T> void addContextProjection(String name, Class<T> type, T target) {
    ModuleDefault module = DefaultModules.currentSilently();
    if (module == null) {
      return;
    }
    module.projectionRegistry().addContextProjection(name, type, target);
  }

  static void removeContextProjection(String name) {
    ModuleDefault module = DefaultModules.currentSilently();
    if (module == null) {
      return;
    }
    module.projectionRegistry().removeContextProjection(name);
  }
}
