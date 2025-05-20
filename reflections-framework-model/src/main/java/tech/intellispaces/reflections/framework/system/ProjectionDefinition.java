package tech.intellispaces.reflections.framework.system;

/**
 * The module projection definition.
 */
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
