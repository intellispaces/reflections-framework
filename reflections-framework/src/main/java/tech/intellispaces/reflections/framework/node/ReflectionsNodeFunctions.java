package tech.intellispaces.reflections.framework.node;

import java.io.IOException;
import java.util.List;

import tech.intellispaces.commons.data.Holder;
import tech.intellispaces.commons.data.ResettableSingleHolder;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.framework.ReflectionsFramework;
import tech.intellispaces.reflections.framework.engine.EngineRegistrar;
import tech.intellispaces.reflections.framework.engine.SingleEngineRegistrar;
import tech.intellispaces.reflections.framework.settings.OntologyReference;
import tech.intellispaces.reflections.framework.settings.SettingsFunctions;
import tech.intellispaces.reflections.framework.system.ModuleHandle;

/**
 * Functions related to the application node as a whole.
 */
public class ReflectionsNodeFunctions {
  private static final Holder<ModuleHandle> MODULE_HOLDER = createModuleHolder();
  private static OntologyReference ONTOLOGY_REFERENCE = null;

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

  public static Holder<ModuleHandle> moduleHolder() {
    return MODULE_HOLDER;
  }

  public static EngineRegistrar createEngineRegistrar() {
    return new SingleEngineRegistrar();
  }

  static Holder<ModuleHandle> createModuleHolder() {
    return new ResettableSingleHolder<>();
  }

  private ReflectionsNodeFunctions() {}
}
