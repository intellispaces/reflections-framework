package tech.intellispacesframework.core.system;

public interface ProjectionProvider {

  /**
   * Projection type.
   */
  Class<?> type();

  /**
   * Projection name.
   */
  String name();

  ProjectionProviderType providerType();
}
