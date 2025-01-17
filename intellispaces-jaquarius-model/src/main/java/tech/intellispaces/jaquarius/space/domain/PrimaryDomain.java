package tech.intellispaces.jaquarius.space.domain;

/**
 * The primary domain description.
 */
public interface PrimaryDomain {

  /**
   * The domain type.
   */
  PrimaryDomainType type();

  /**
   * The domain qualified name.
   */
  String domainName();

  /**
   * The object handle class name.
   */
  String handleClassName();
}
