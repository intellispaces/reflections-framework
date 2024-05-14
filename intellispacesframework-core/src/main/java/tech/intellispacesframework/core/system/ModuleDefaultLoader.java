package tech.intellispacesframework.core.system;

class ModuleDefaultLoader implements ModuleLoader {
  private final ModuleDefaultFactory factory = new ModuleDefaultFactory();
  private final ModuleDefaultStarter starter = new ModuleDefaultStarter();

  @Override
  public Module loadModule(Class<?> moduleClass, String[] args) {
    ModuleDefault module = factory.createModule(moduleClass, args);
    Modules.setActiveModule(module);
    starter.startModule(module);
    return module;
  }
}
