package tech.intellispaces.jaquarius.object.reference;

import tech.intellispaces.commons.entity.Enumeration;

/**
 * Object reference forms.
 */
public enum ObjectReferenceForms implements ObjectReferenceForm, Enumeration<ObjectReferenceForm> {

  /**
   * The object reference represented as plain object interface or primitive type.
   */
  Plain,

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

  public static ObjectReferenceForms from(ObjectReferenceForm value) {
    return VALUES[value.ordinal()];
  }

  private static final ObjectReferenceForms[] VALUES = values();
}
