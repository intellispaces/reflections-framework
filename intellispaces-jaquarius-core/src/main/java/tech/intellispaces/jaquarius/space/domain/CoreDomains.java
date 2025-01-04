package tech.intellispaces.jaquarius.space.domain;

import tech.intellispaces.general.entity.Enumeration;

public enum CoreDomains implements Enumeration<CoreDomain>, CoreDomain {

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


  private final String classname;

  CoreDomains(java.lang.String classname) {
    this.classname = classname;
  }

  public java.lang.String className() {
    return classname;
  }
}
