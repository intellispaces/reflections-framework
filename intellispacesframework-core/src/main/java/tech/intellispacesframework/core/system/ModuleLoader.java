package tech.intellispacesframework.core.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModuleLoader {
  private static final ModuleDefaultFactory factory = new ModuleDefaultFactory();
  private static final Logger LOG = LoggerFactory.getLogger(ModuleLoader.class);

  public static ModuleDefault loadDefaultModule(Class<?> moduleClass) {
    ModuleDefault activeModule = Modules.activeModuleSilently();
    if (activeModule != null) {
      LOG.warn("Active module has already been loaded into application. Current active module will be reloaded");
      activeModule.stop();
      Modules.setActiveModule(null);
    }

    ModuleDefault newModule = factory.createModule(moduleClass);
    Modules.setActiveModule(newModule);
    return newModule;
  }
}
