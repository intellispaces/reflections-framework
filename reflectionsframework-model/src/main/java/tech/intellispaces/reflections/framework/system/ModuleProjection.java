package tech.intellispaces.reflections.framework.system;

public interface ModuleProjection {

  /**
   * Projection name.
   */
  String name();

  /**
   * Projection type.
   */
  Class<?> type();

  /**
   * Projection target.
   */
  Object target();

  /**
   * Projection definition.
   */
  ProjectionDefinition definition();
}
