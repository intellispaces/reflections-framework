package tech.intellispaces.jaquarius.object.reference;

import tech.intellispaces.general.entity.Enumeration;

public enum ObjectReferenceForms implements ObjectReferenceForm, Enumeration<ObjectReferenceForm> {

  /**
   * The object reference represented as the pure Java object or instance of the class {@link ObjectHandle}.
   */
  Common,

  /**
   * The object reference represented as the primitive value.
   */
  Primitive
}
