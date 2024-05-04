package tech.intellispacesframework.core.system;

/**
 * Default system module factory.
 */
class ModuleDefaultFactory implements SystemModuleFactory {
  private final ModuleDefaultAssembler moduleAssembler;
  private final ModuleDefaultLoader moduleLoader;
  private final ModuleValidator moduleValidator;

  public ModuleDefaultFactory(
      ModuleDefaultAssembler moduleAssembler,
      ModuleDefaultLoader moduleLoader,
      ModuleValidator moduleValidator
  ) {
    this.moduleAssembler = moduleAssembler;
    this.moduleLoader = moduleLoader;
    this.moduleValidator = moduleValidator;
  }

  @Override
  public ModuleDefault createModule(Class<?> moduleClass, String[] args) {
    ModuleDefault module = moduleAssembler.assembleModule(moduleClass, args);
    moduleValidator.validateModule(module);
    moduleLoader.loadModule(module);
    return module;
  }
}
