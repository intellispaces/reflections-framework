package tech.intellispaces.reflections.framework.settings;

import tech.intellispaces.core.Rid;

/**
 * The domain reference point.
 */
public interface DomainReference {

  /**
   * The domain assignment.
   */
  DomainAssignment assignment();

  Rid domainId();

  /**
   * The domain qualified alias.
   */
  String domainAlias();

  /**
   * The domain class canonical name.
   */
  String classCanonicalName();

  /**
   * The delegate class canonical name.
   */
  String delegateClassName();
}
