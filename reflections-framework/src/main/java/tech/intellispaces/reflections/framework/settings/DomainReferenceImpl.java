package tech.intellispaces.reflections.framework.settings;

import tech.intellispaces.core.Rid;

class DomainReferenceImpl implements DomainReference {
  private final DomainAssignments type;
  private final Rid domainId;
  private final String domainAlias;
  private final String classCanonicalName;

  DomainReferenceImpl(
      DomainAssignments type,
      Rid domainId,
      String domainAlias,
      String classCanonicalName
  ) {
    this.type = type;
    this.domainId = domainId;
    this.domainAlias = domainAlias;
    this.classCanonicalName = classCanonicalName;
  }

  @Override
  public DomainAssignment assignment() {
    return type;
  }

  @Override
  public Rid domainId() {
    return domainId;
  }

  @Override
  public String domainAlias() {
    return domainAlias;
  }

  @Override
  public String classCanonicalName() {
    return classCanonicalName;
  }

  @Override
  public String delegateClassName() {
    return type.delegateClassName();
  }
}
