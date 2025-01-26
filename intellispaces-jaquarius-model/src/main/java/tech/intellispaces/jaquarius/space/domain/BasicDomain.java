package tech.intellispaces.jaquarius.space.domain;

/**
 * The basic domain description.
 */
public interface BasicDomain {

  /**
   * The basic domain qualified name.
   */
  String domainName();

  /**
   * The domain purpose.
   */
  BasicDomainPurpose purpose();

  /**
   * The delegate class canonical name.
   */
  String delegateClassName();
}
