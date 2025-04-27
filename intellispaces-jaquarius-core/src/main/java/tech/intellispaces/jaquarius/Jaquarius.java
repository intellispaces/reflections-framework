package tech.intellispaces.jaquarius;

import java.io.IOException;
import java.util.List;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.jaquarius.settings.OntologyReferences;
import tech.intellispaces.jaquarius.settings.SettingsFunctions;
import tech.intellispaces.jaquarius.system.Module;
import tech.intellispaces.jaquarius.system.Modules;

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
   * Returns ontology references.
   */
  public static OntologyReferences ontologyReferences() {
    if (ONTOLOGY_REFERENCES == null) {
      try {
        List<OntologyReferences> ontologyReferencesList = SettingsFunctions.loadOntologyReferences(Jaquarius.class.getClassLoader());
        ONTOLOGY_REFERENCES = SettingsFunctions.mergeOntologyReferences(ontologyReferencesList);
      } catch (IOException e) {
        throw UnexpectedExceptions.withCauseAndMessage(e, "Unable to load ontology references");
      }
    }
    return ONTOLOGY_REFERENCES;
  }

  public static void ontologyReferences(OntologyReferences ontologyReferences) {
    ONTOLOGY_REFERENCES = ontologyReferences;
  }

  private static OntologyReferences ONTOLOGY_REFERENCES = null;
}
