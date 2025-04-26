package tech.intellispaces.jaquarius;

import java.io.IOException;
import java.util.List;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.properties.PropertiesSet;
import tech.intellispaces.jaquarius.settings.OntologyDescription;
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
   * Returns ontology description.
   */
  public static OntologyDescription ontologyDescription() {
    if (ONTOLOGY_DESCRIPTION == null) {
      try {
        List<PropertiesSet> propsList = SettingsFunctions.loadOntologyDescriptionProps(Jaquarius.class.getClassLoader());
        List<OntologyDescription> ontologyDescriptions = propsList.stream()
                .map(SettingsFunctions::parseOntologyDescription)
                .toList();
        ONTOLOGY_DESCRIPTION = SettingsFunctions.mergeOntologyDescriptions(ontologyDescriptions);
      } catch (IOException e) {
        throw UnexpectedExceptions.withCauseAndMessage(e, "Unable to load Jaquarius settings");
      }
    }
    return ONTOLOGY_DESCRIPTION;
  }

  public static void ontologyDescription(OntologyDescription ontologyDescription) {
    ONTOLOGY_DESCRIPTION = ontologyDescription;
  }

  private static OntologyDescription ONTOLOGY_DESCRIPTION = null;
}
