package tech.intellispaces.reflections.framework;

import java.io.IOException;
import java.util.List;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.framework.settings.OntologyReference;
import tech.intellispaces.reflections.framework.settings.SettingsFunctions;
import tech.intellispaces.reflections.framework.system.Module;
import tech.intellispaces.reflections.framework.system.Modules;

public class ReflectionsFramework {

  /**
   * Returns the current loaded system.
   */
  public static Object system() {
    return null;
  }

  /**
   * Loads a new system module.
   *
   * @param moduleClass the module class.
   * @param args command line arguments.
   * @return the loaded module.
   */
  public static Module loadModule(Class<?> moduleClass, String[] args) {
    return Modules.load(moduleClass, args);
  }

  /**
   * Loads a new system module.
   *
   * @param unitClasses unit classes.
   * @return the loaded module.
   */
  public static Module loadModule(Class<?>... unitClasses) {
    return Modules.load(unitClasses);
  }

  /**
   * Loads a new system module.
   *
   * @param unitClasses unit classes.
   * @param args command line arguments.
   * @return the loaded module.
   */
  public static Module loadModule(List<Class<?>> unitClasses, String[] args) {
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
