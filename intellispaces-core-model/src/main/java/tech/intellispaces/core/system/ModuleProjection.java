package tech.intellispaces.core.system;

public interface ModuleProjection {

  /**
   * Projection name.
   */
  String name();

  /**
   * Projection target class.
   */
  Class<?> targetClass();

  /**
   * Projection target.
   */
  Object target();

  /**
   * Projection definition.
   */
  ProjectionDefinition definition();
}
