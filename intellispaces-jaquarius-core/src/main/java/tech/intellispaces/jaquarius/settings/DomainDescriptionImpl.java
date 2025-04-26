package tech.intellispaces.jaquarius.settings;

class DomainDescriptionImpl implements DomainDescription {
  private final DomainTypes type;
  private final String name;

  DomainDescriptionImpl(DomainTypes type, String name) {
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
