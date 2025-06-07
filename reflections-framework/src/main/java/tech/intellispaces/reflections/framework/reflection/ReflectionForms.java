package tech.intellispaces.reflections.framework.reflection;

import tech.intellispaces.commons.abstraction.Enumeration;

/**
 * Reflection forms.
 */
public enum ReflectionForms implements ReflectionForm, Enumeration<ReflectionForm> {

  /**
   * The form represented as reflection or primitive type.
   */
  Reflection,

  /**
   * The form represented as primitive type.
   */
  Primitive,

  /**
   * The form represented as object wrapper of primitive type.
   */
  PrimitiveWrapper;

  public static ReflectionForms of(ReflectionForm value) {
    return VALUES[value.ordinal()];
  }

  private static final ReflectionForms[] VALUES = values();
}
