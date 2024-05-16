package tech.intellispacesframework.core.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ModuleDefaultLoader implements ModuleLoader {
  private final ModuleDefaultFactory factory = new ModuleDefaultFactory();
  private final ModuleDefaultStarter starter = new ModuleDefaultStarter();
  private static final Logger LOG = LoggerFactory.getLogger(ModuleDefaultLoader.class);

  @Override
  public Module loadModule(Class<?> moduleClass, String[] args) {
    Module activeModule = Modules.activeModuleSilently();
    if (activeModule != null) {
      LOG.warn("Active module has already been loaded into application. Current active module will be reloaded");
      activeModule.shutdown();
      Modules.setActiveModule(null);
    }

    ModuleDefault newModule = factory.createModule(moduleClass, args);
    Modules.setActiveModule(newModule);
    starter.startModule(newModule);
    return newModule;
  }
}
