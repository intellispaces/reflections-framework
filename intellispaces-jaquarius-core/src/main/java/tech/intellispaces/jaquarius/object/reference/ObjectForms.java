package tech.intellispaces.jaquarius.object.reference;

import tech.intellispaces.commons.entity.Enumeration;

/**
 * Object reference forms.
 */
public enum ObjectForms implements ObjectForm, Enumeration<ObjectForm> {

  /**
   * The object reference represented as simple object interface or primitive type.
   */
  Simple,

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

  public static ObjectForms from(ObjectForm value) {
    return VALUES[value.ordinal()];
  }

  private static final ObjectForms[] VALUES = values();
}
