package tech.intellispaces.jaquarius.space.domain;

class BasicDomainImpl implements BasicDomain {
  private final String domainName;
  private final BasicDomainPurposes purpose;

  BasicDomainImpl(String domainName, BasicDomainPurposes purpose) {
    this.domainName = domainName;
    this.purpose = purpose;
  }

  @Override
  public String domainName() {
    return domainName;
  }

  @Override
  public BasicDomainPurpose purpose() {
    return purpose;
  }

  @Override
  public String delegateClassName() {
    return purpose.delegateClassName();
  }
}
