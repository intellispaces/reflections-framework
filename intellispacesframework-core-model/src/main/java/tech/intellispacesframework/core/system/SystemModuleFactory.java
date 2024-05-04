package tech.intellispacesframework.core.system;

public interface SystemModuleFactory {

  /**
   * Creates system module.
   *
   * @param moduleClass the module class.
   * @param args command line arguments.
   * @return system module.
   */
  Module createModule(Class<?> moduleClass, String[] args);
}
