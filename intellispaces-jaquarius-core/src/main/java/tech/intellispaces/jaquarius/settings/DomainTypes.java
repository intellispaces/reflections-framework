package tech.intellispaces.jaquarius.settings;

import tech.intellispaces.commons.abstraction.Enumeration;

public enum DomainTypes implements Enumeration<DomainType>, DomainType {

  /**
   * The domain of notions.
   */
  Notion("java.lang.Object"),

  /**
   * The domain of domains.
   */
  Domain("tech.intellispaces.commons.type.Type"),

  /**
   * The domain of boolean.
   */
  Boolean("java.lang.Boolean"),

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
  Byte("java.lang.Byte"),

  /**
   * The domain of 16 bits integer numbers.
   */
  Short("java.lang.Short"),

  /**
   * The domain of 32 bits integer numbers.
   */
  Integer("java.lang.Integer"),

  /**
   * The domain of 64 bits integer numbers.
   */
  Long("java.lang.Long"),

  /**
   * The domain of 32 bits float numbers.
   */
  Float("java.lang.Float"),

  /**
   * The domain of 64 bits float numbers.
   */
  Double("java.lang.Double"),

  /**
   * The properties set domain.
   */
  PropertiesSet(null),

  /**
   * The dataset domain.
   */
  Dataset(null);

  private final String delegateClassName;

  DomainTypes(java.lang.String delegateClassName) {
    this.delegateClassName = delegateClassName;
  }

  public java.lang.String delegateClassName() {
    return delegateClassName;
  }
}
