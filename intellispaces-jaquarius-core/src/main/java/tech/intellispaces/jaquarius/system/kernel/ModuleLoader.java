package tech.intellispaces.jaquarius.system.kernel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.intellispaces.jaquarius.system.Module;
import tech.intellispaces.jaquarius.system.Modules;

import java.util.List;

public class ModuleLoader {
  private static final ModuleFactory FACTORY = new ModuleFactory();
  private static final ModuleValidator MODULE_VALIDATOR = new ModuleValidator();
  private static final Logger LOG = LoggerFactory.getLogger(ModuleLoader.class);

  public static Module loadModule(List<Class<?>> unitClasses, String[] args) {
    Module currentModule = Modules.currentSilently();
    if (currentModule != null) {
      LOG.warn("Current module has already been loaded into application. Current active module will be reloaded");
    }

    KernelModule newModule = FACTORY.createModule(unitClasses);
    MODULE_VALIDATOR.validate(newModule);
    if (currentModule != null) {
      currentModule.stop();
      KernelFunctions.setCurrentModule(null);
    }
    KernelFunctions.setCurrentModule(newModule);
    return newModule.module();
  }

  public static void unloadModule() {
    KernelModule currentModule = KernelFunctions.currentModuleSilently();
    if (currentModule != null) {
      currentModule.stop();
    }
    KernelFunctions.setCurrentModule(null);
  }
}
