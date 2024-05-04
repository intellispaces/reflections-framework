package tech.intellispacesframework.core.system;

public class ModuleFactories {
  private static SystemModuleFactory MODULE_FACTORY;

  public static SystemModuleFactory get() {
    if (MODULE_FACTORY == null) {
      MODULE_FACTORY = buildSystemModuleDefaultFactory();
    }
    return MODULE_FACTORY;
  }

  static ModuleDefaultFactory buildSystemModuleDefaultFactory() {
    var unitDeclarationValidator = new UnitDeclarationValidator();
    var moduleValidator = new ModuleValidator();
    var moduleAssembler = new ModuleDefaultAssembler(unitDeclarationValidator);
    var moduleLoader = new ModuleDefaultLoader();
    return new ModuleDefaultFactory(moduleAssembler, moduleLoader, moduleValidator);
  }

  private ModuleFactories() {}
}
