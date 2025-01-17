package tech.intellispaces.jaquarius.space.domain;

class PrimaryDomainImpl implements PrimaryDomain {
  private final PrimaryDomainTypes type;
  private final String domainName;

  PrimaryDomainImpl(PrimaryDomainTypes type, String domainName) {
    this.type = type;
    this.domainName = domainName;
  }

  @Override
  public PrimaryDomainType type() {
    return type;
  }

  @Override
  public String domainName() {
    return domainName;
  }

  @Override
  public String handleClassName() {
    return type.handleClassName();
  }
}
