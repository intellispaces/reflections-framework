package tech.intellispaces.reflections.framework.settings;

class DomainReferenceImpl implements DomainReference {
  private final DomainAssignments type;
  private final String domainName;
  private final String classCanonicalName;

  DomainReferenceImpl(
      DomainAssignments type,
      String domainName,
      String classCanonicalName
  ) {
    this.type = type;
    this.domainName = domainName;
    this.classCanonicalName = classCanonicalName;
  }

  @Override
  public DomainAssignment assignment() {
    return type;
  }

  @Override
  public String domainName() {
    return domainName;
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
