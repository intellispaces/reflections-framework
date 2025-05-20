package tech.intellispaces.reflections.framework.system.projection;

public interface DirectProjectionDefinitions {

  static DirectProjectionDefinition get(String name, Class<?> type, Object target) {
    return new DirectProjectionDefinition(name, type, target);
  }
}
