package tech.intellispaces.jaquarius.settings;

/**
 * The key domain.
 */
public interface KeyDomain {

  /**
   * The key domain purpose.
   */
  KeyDomainPurpose purpose();

  /**
   * The key domain full qualified name.
   */
  String domainName();

  /**
   * The delegate class canonical name.
   */
  String delegateClassName();
}
