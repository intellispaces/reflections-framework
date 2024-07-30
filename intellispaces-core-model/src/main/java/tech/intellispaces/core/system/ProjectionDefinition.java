package tech.intellispaces.core.system;

public interface ProjectionDefinition {

  /**
   * Projection name.
   */
  String name();

  /**
   * Projection type.
   */
  Class<?> type();

  boolean isLazy();

  ProjectionDefinitionKind kind();
}
