package tech.intellispaces.reflections.framework.settings;

/**
 * The domain reference point.
 */
public interface DomainReference {

  /**
   * The domain assignment.
   */
  DomainAssignment assignment();

  /**
   * The full qualified name.
   */
  String domainName();

  /**
   * The domain class canonical name.
   */
  String classCanonicalName();

  /**
   * The delegate class canonical name.
   */
  String delegateClassName();
}
