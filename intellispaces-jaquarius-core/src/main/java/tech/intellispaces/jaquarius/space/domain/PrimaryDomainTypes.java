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
   * The domain of integer numbers.
   */
  Integer("java.lang.Integer");

  private final String handleClassName;

  PrimaryDomainTypes(java.lang.String handleClassName) {
    this.handleClassName = handleClassName;
  }

  public java.lang.String handleClassName() {
    return handleClassName;
  }
}
