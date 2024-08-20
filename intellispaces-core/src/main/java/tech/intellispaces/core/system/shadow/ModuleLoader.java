package tech.intellispaces.core.system.shadow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.intellispaces.core.system.Module;
import tech.intellispaces.core.system.Modules;

import java.util.Arrays;
import java.util.List;

public class ModuleLoader {
  private static final ModuleFactory factory = new ModuleFactory();
  private static final ModuleValidator moduleValidator = new ModuleValidator();

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

    ShadowModule newModule = factory.createModule(unitClasses);
    moduleValidator.validate(newModule);
    if (currentModule != null) {
      currentModule.stop();
      ShadowModules.setCurrentModule(null);
    }
    ShadowModules.setCurrentModule(newModule);

    newModule.start(args);
  }

  public static void unloadModule() {
    Module currentModule = ShadowModules.currentSilently();
    if (currentModule != null) {
      currentModule.stop();
    }
    ShadowModules.setCurrentModule(null);
  }
}
