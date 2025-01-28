package tech.intellispaces.jaquarius.object.reference;

import tech.intellispaces.general.entity.Enumeration;

public enum ObjectHandleMethodForms implements ObjectHandleMethodForm, Enumeration<ObjectHandleMethodForm> {

  Object,

  /**
   * The object reference represented as instance of the class {@link ObjectHandle} or primitive type.
   */
  Normal
}
