package tech.intellispaces.core.system;

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
