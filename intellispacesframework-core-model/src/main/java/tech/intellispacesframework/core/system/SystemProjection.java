package tech.intellispacesframework.core.system;

public interface SystemProjection {

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
   * Projection provider.
   */
  ProjectionDefinition provider();
}
