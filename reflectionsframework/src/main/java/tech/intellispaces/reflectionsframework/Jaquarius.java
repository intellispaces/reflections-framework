package tech.intellispaces.reflectionsframework;

import java.io.IOException;
import java.util.List;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflectionsframework.settings.OntologyReference;
import tech.intellispaces.reflectionsframework.settings.SettingsFunctions;
import tech.intellispaces.reflectionsframework.system.Module;
import tech.intellispaces.reflectionsframework.system.Modules;

public class Jaquarius {

  public static void loadContext() {

  }

  /**
   * Creates new system module.
   *
   * @param moduleClass the module class.
   * @param args command line arguments.
   * @return the created module.
   */
  public static Module createModule(Class<?> moduleClass, String[] args) {
    return Modules.create(moduleClass, args);
  }

  /**
   * Creates new system module.
   *
   * @param unitClasses unit classes.
   * @return the created module.
   */
  public static Module createModule(Class<?>... unitClasses) {
    return Modules.create(unitClasses);
  }

  /**
   * Creates new system module.
   *
   * @param unitClasses unit classes.
   * @param args command line arguments.
   * @return the created module.
   */
  public static Module createModule(List<Class<?>> unitClasses, String[] args) {
    return Modules.create(unitClasses, args);
  }

  /**
   * Releases current module.
   */
  public static void releaseModule() {
    Modules.unload();
  }

  public static void createStartAndStopModule(Class<?>... unitClasses) {
    try {
      createModule(unitClasses).start();
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
            Jaquarius.class.getClassLoader());
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
