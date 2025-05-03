package tech.intellispaces.reflections.object.reference;

import tech.intellispaces.commons.abstraction.Enumeration;

/**
 * Object reference forms.
 */
public enum ObjectReferenceForms implements ObjectReferenceForm, Enumeration<ObjectReferenceForm> {

  /**
   * The object reference represented as regular object interface or primitive type.
   */
  Regular,

  /**
   * The object reference represented as object handle or primitive type.
   */
  ObjectHandle,

  /**
   * The object reference represented as primitive type.
   */
  Primitive,

  /**
   * The object reference represented as object wrapper of primitive type.
   */
  PrimitiveWrapper;

  public static ObjectReferenceForms of(ObjectReferenceForm value) {
    return VALUES[value.ordinal()];
  }

  private static final ObjectReferenceForms[] VALUES = values();
}
