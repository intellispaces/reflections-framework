package tech.intellispaces.jaquarius.settings;

class KeyDomainImpl implements KeyDomain {
  private final KeyDomainPurposes purpose;
  private final String domainName;

  KeyDomainImpl(KeyDomainPurposes purpose, String domainName) {
    this.purpose = purpose;
    this.domainName = domainName;
  }

  @Override
  public KeyDomainPurpose purpose() {
    return purpose;
  }

  @Override
  public String domainName() {
    return domainName;
  }

  @Override
  public String delegateClassName() {
    return purpose.delegateClassName();
  }
}
