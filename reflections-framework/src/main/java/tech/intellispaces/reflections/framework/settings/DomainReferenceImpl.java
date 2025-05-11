package tech.intellispaces.reflections.framework.settings;

class DomainReferenceImpl implements DomainReference {
  private final DomainTypes type;
  private final String name;

  DomainReferenceImpl(DomainTypes type, String name) {
    this.type = type;
    this.name = name;
  }

  @Override
  public DomainType type() {
    return type;
  }

  @Override
  public String domainName() {
    return name;
  }

  @Override
  public String delegateClassName() {
    return type.delegateClassName();
  }
}
