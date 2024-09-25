package intellispaces.framework.core.system.kernel;

import intellispaces.framework.core.system.Module;
import intellispaces.framework.core.system.Modules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ModuleLoader {
  private static final ModuleFactory FACTORY = new ModuleFactory();
  private static final ModuleValidator MODULE_VALIDATOR = new ModuleValidator();
  private static final Logger LOG = LoggerFactory.getLogger(ModuleLoader.class);

  public static void loadModule(Class<?> moduleClass, String[] args) {
    loadModule(List.of(moduleClass), args);
  }

  public static void loadModule(Class<?>... unitClasses) {
    loadModule(Arrays.stream(unitClasses).toList(), new String[0]);
  }

  public static void loadModule(List<Class<?>> unitClasses, String[] args) {
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

    newModule.start(args);
  }

  public static void unloadModule() {
    Module currentModule = KernelFunctions.currentModuleSilently();
    if (currentModule != null) {
      currentModule.stop();
    }
    KernelFunctions.setCurrentModule(null);
  }
}
