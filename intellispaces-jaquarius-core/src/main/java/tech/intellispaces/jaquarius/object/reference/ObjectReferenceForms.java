package tech.intellispaces.jaquarius.object.reference;

import tech.intellispaces.commons.entity.Enumeration;

public enum ObjectReferenceForms implements ObjectReferenceForm, Enumeration<ObjectReferenceForm> {

  /**
   * The object reference represented as object or primitive.
   */
  Default,

  /**
   * The object reference represented as the primitive type.
   */
  Primitive
}
