package tech.intellispaces.framework.core.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModuleLoader {
  private static final ModuleDefaultFactory factory = new ModuleDefaultFactory();
  private static final ModuleValidator moduleValidator = new ModuleValidator();

  private static final Logger LOG = LoggerFactory.getLogger(ModuleLoader.class);

  public static ModuleDefault loadDefaultModule(Class<?>... unitClasses) {
    ModuleDefault activeModule = Modules.activeModuleSilently();
    if (activeModule != null) {
      LOG.warn("Active module has already been loaded into application. Current active module will be reloaded");
    }

    ModuleDefault newModule = factory.createModule(unitClasses);
    moduleValidator.validate(newModule);
    if (activeModule != null) {
      activeModule.stop();
      Modules.setActiveModule(null);
    }
    Modules.setActiveModule(newModule);
    return newModule;
  }
}
