package tech.intellispaces.framework.core.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.intellispaces.framework.core.validate.ModuleValidator;

public class ModuleLoader {
  private static final ModuleDefaultFactory factory = new ModuleDefaultFactory();
  private static final ModuleValidator moduleValidator = new ModuleValidator();

  private static final Logger LOG = LoggerFactory.getLogger(ModuleLoader.class);

  public static ModuleDefault loadDefaultModule(Class<?> moduleClass) {
    ModuleDefault activeModule = Modules.activeModuleSilently();
    if (activeModule != null) {
      LOG.warn("Active module has already been loaded into application. Current active module will be reloaded");
    }

    ModuleDefault newModule = factory.createModule(moduleClass);
    moduleValidator.validateModuleInstance(newModule);
    if (activeModule != null) {
      activeModule.stop();
      Modules.setActiveModule(null);
    }
    Modules.setActiveModule(newModule);
    return newModule;
  }
}
