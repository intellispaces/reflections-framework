package tech.intellispaces.reflections.framework.settings;

class DomainReferenceImpl implements DomainReference {
  private final DomainTypes type;
  private final String domainName;
  private final String classCanonicalName;

  DomainReferenceImpl(
      DomainTypes type,
      String domainName,
      String classCanonicalName
  ) {
    this.type = type;
    this.domainName = domainName;
    this.classCanonicalName = classCanonicalName;
  }

  @Override
  public DomainType type() {
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
