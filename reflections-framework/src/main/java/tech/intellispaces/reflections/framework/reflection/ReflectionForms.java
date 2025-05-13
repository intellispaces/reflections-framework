package tech.intellispaces.reflections.framework.reflection;

import tech.intellispaces.commons.abstraction.Enumeration;

/**
 * Reflection forms.
 */
public enum ReflectionForms implements ReflectionForm, Enumeration<ReflectionForm> {

  /**
   * The object reference represented as object handle or primitive type.
   */
  Reflection,

  /**
   * The object reflection represented as primitive type.
   */
  Primitive,

  /**
   * The object reflection represented as object wrapper of primitive type.
   */
  PrimitiveWrapper,

  /**
   * The object reflection represented as regular object interface or primitive type.
   */
  Regular;

  public static ReflectionForms of(ReflectionForm value) {
    return VALUES[value.ordinal()];
  }

  private static final ReflectionForms[] VALUES = values();
}
