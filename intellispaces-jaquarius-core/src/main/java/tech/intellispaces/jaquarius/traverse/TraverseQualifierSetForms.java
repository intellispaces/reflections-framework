package tech.intellispaces.jaquarius.traverse;

import tech.intellispaces.general.entity.Enumeration;
import tech.intellispaces.jaquarius.object.reference.ObjectHandle;

public enum TraverseQualifierSetForms implements TraverseQualifierSetForm, Enumeration<TraverseQualifierSetForm> {

  Object,

  /**
   * The object reference represented as instance of the class {@link ObjectHandle} or primitive type.
   */
  Normal
}
