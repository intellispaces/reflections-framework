package tech.intellispaces.reflections.framework.settings;

/**
 * The domain reference description.
 */
public interface DomainReference {

  /**
   * The domain type.
   */
  DomainType type();

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
