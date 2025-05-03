package tech.intellispaces.reflectionsj.system.projection;

public interface ProjectionDefinitionBasedOnProviderClasses {

  static ProjectionDefinitionBasedOnProviderClass get(
      Class<?> unitClass,
      String name,
      Class<?> type,
      boolean lazy,
      String providerClassCanonicalName
  ) {
    return new ProjectionDefinitionBasedOnProviderClass(
      unitClass, name, type, lazy, providerClassCanonicalName
    );
  }
}
