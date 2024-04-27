package tech.intellispacesframework.core.system;

public interface SystemModuleFactory {

  /**
   * Creates system module.
   *
   * @param moduleClass the module class.
   * @param args command line arguments.
   * @return system module.
   */
  SystemModule createModule(Class<?> moduleClass, String[] args);
}
