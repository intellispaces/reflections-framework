package tech.intellispacesframework.core.system;

public interface ProjectionProvider {

  /**
   * Projection name.
   */
  String name();

  /**
   * Projection type.
   */
  Class<?> type();

  ProjectionProviderType providerType();
}
