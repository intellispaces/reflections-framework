package tech.intellispacesframework.core.system;

/**
 * Default system module factory.
 */
class SystemModuleDefaultFactory implements SystemModuleFactory {
  private final SystemModuleDefaultAssembler moduleAssembler;
  private final SystemModuleDefaultLoader moduleLoader;
  private final ModuleValidator moduleValidator;

  public SystemModuleDefaultFactory(
      SystemModuleDefaultAssembler moduleAssembler,
      SystemModuleDefaultLoader moduleLoader,
      ModuleValidator moduleValidator
  ) {
    this.moduleAssembler = moduleAssembler;
    this.moduleLoader = moduleLoader;
    this.moduleValidator = moduleValidator;
  }

  @Override
  public SystemModuleDefault createModule(Class<?> moduleClass, String[] args) {
    SystemModuleDefault module = moduleAssembler.assembleModule(moduleClass, args);
    moduleValidator.validateModule(module);
    moduleLoader.loadModule(module);
    return module;
  }
}
