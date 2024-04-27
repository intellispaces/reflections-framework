package tech.intellispacesframework.core.system;

public class ModuleFactories {
  private static SystemModuleFactory MODULE_FACTORY;

  public static SystemModuleFactory get() {
    if (MODULE_FACTORY == null) {
      MODULE_FACTORY = buildSystemModuleDefaultFactory();
    }
    return MODULE_FACTORY;
  }

  static SystemModuleDefaultFactory buildSystemModuleDefaultFactory() {
    var unitDeclarationValidator = new UnitDeclarationValidator();
    var moduleValidator = new ModuleValidator();
    var moduleAssembler = new SystemModuleDefaultAssembler(unitDeclarationValidator);
    var moduleLoader = new SystemModuleDefaultLoader();
    return new SystemModuleDefaultFactory(moduleAssembler, moduleLoader, moduleValidator);
  }

  private ModuleFactories() {}
}
