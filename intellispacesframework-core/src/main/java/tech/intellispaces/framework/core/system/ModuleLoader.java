package tech.intellispaces.framework.core.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ModuleLoader {
  private static final ModuleDefaultFactory factory = new ModuleDefaultFactory();
  private static final ModuleValidator moduleValidator = new ModuleValidator();

  private static final Logger LOG = LoggerFactory.getLogger(ModuleLoader.class);

  public static void loadModule(Class<?> moduleClass, String[] args) {
    loadModule(List.of(moduleClass), args);
  }

  public static void loadModule(Class<?>... unitClasses) {
    loadModule(Arrays.stream(unitClasses).toList(), new String[0]);
  }

  public static void loadModule(List<Class<?>> unitClasses, String[] args) {
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

    newModule.start(args);
  }

  public static void unloadModule() {
    Module activeModule = Modules.activeModuleSilently();
    if (activeModule != null) {
      activeModule.stop();
    }
    Modules.setActiveModule(null);
  }
}
