package tech.intellispaces.reflections.framework.system.projection;

public interface ProviderClassesProjectionDefinitions {

  static ProviderClassProjectionDefinition get(
      Class<?> unitClass,
      String name,
      Class<?> type,
      boolean lazy,
      String providerClassCanonicalName
  ) {
    return new ProviderClassProjectionDefinition(
      unitClass, name, type, lazy, providerClassCanonicalName
    );
  }
}
