package tech.intellispaces.reflectionsj.settings;

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
   * The delegate class canonical name.
   */
  String delegateClassName();
}
