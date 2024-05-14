package tech.intellispacesframework.core.system;

public interface ModuleLoader {

  Module loadModule(Class<?> moduleClass, String[] args);
}
