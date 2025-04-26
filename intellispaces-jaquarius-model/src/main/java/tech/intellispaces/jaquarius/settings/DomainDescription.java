package tech.intellispaces.jaquarius.settings;

/**
 * The domain description.
 */
public interface DomainDescription {

  /**
   * The domain type.
   */
  DomainType type();

  /**
   * The full qualified name.
   */
  String domainName();

  /**
   * The delegate class canonical name.
   */
  String delegateClassName();
}
