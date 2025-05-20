package tech.intellispaces.reflections.framework;

import java.io.IOException;
import java.util.List;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.framework.settings.OntologyReference;
import tech.intellispaces.reflections.framework.settings.SettingsFunctions;
import tech.intellispaces.reflections.framework.system.ModuleHandle;
import tech.intellispaces.reflections.framework.system.Modules;
import tech.intellispaces.reflections.framework.system.System;

/**
 * The reflections framework.
 */
public class ReflectionsFramework {

  /**
   * Returns the current system.
   * <p>
   * If there is no current system, an exception will be thrown.
   */
  public static System system() {
    return Modules.currentSystem();
  }

  /**
   * Returns the current module.
   * <p>
   * If there is no current module, an exception will be thrown.
   */
  public static ModuleHandle module() {
    return Modules.current();
  }

  /**
   * Loads a new system module.
   *
   * @param moduleClass the module class.
   * @param args command line arguments.
   * @return the loaded module.
   */
  public static ModuleHandle loadModule(Class<?> moduleClass, String[] args) {
    return Modules.load(moduleClass, args);
  }

  /**
   * Loads a new system module.
   *
   * @param unitClasses unit classes.
   * @return the loaded module.
   */
  public static ModuleHandle loadModule(Class<?>... unitClasses) {
    return Modules.load(unitClasses);
  }

  /**
   * Loads a new system module.
   *
   * @param unitClasses unit classes.
   * @param args command line arguments.
   * @return the loaded module.
   */
  public static ModuleHandle loadModule(List<Class<?>> unitClasses, String[] args) {
    return Modules.load(unitClasses, args);
  }

  /**
   * Uploads current loaded module.
   */
  public static void uploadModule() {
    Modules.unload();
  }

  /**
   * Creates, starts and then stops and uploads module.
   *
   * @param unitClasses module unit classes.
   */
  public static void flashModule(Class<?>... unitClasses) {
    try {
      loadModule(unitClasses).start();
    } finally {
      Modules.unload();
    }
  }

  /**
   * Returns ontology reference.
   */
  public static OntologyReference ontologyReference() {
    if (ONTOLOGY_REFERENCE == null) {
      try {
        List<OntologyReference> ontologyReferences = SettingsFunctions.loadOntologyReferences(
            ReflectionsFramework.class.getClassLoader());
        ONTOLOGY_REFERENCE = SettingsFunctions.mergeOntologyReferences(ontologyReferences);
      } catch (IOException e) {
        throw UnexpectedExceptions.withCauseAndMessage(e, "Unable to load ontology reference");
      }
    }
    return ONTOLOGY_REFERENCE;
  }

  public static void ontologyReference(OntologyReference ontologyReference) {
    ONTOLOGY_REFERENCE = ontologyReference;
  }

  private static OntologyReference ONTOLOGY_REFERENCE = null;
}
