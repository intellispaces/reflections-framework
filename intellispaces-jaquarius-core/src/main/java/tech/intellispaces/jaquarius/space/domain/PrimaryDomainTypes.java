package tech.intellispaces.jaquarius.space.domain;

import tech.intellispaces.general.entity.Enumeration;

public enum PrimaryDomainTypes implements Enumeration<PrimaryDomainType>, PrimaryDomainType {

  /**
   * The domain of domains.
   */
  Domain("tech.intellispaces.general.type.Type"),

  /**
   * The domain of strings.
   */
  String("java.lang.String"),

  /**
   * The domain of numbers.
   */
  Number("java.lang.Number"),

  /**
   * The domain of 8 bits integer numbers.
   */
  Integer8("java.lang.Byte"),

  /**
   * The domain of 16 bits integer numbers.
   */
  Integer16("java.lang.Short"),

  /**
   * The domain of 32 bits integer numbers.
   */
  Integer32("java.lang.Integer"),

  /**
   * The domain of 64 bits integer numbers.
   */
  Integer64("java.lang.Long"),

  /**
   * The domain of 32 bits float numbers.
   */
  Float32("java.lang.Float"),

  /**
   * The domain of 64 bits float numbers.
   */
  Float64("java.lang.Double");

  private final String handleClassName;

  PrimaryDomainTypes(java.lang.String handleClassName) {
    this.handleClassName = handleClassName;
  }

  public java.lang.String handleClassName() {
    return handleClassName;
  }
}
