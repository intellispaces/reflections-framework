package tech.intellispaces.jaquarius.space.domain;

import java.util.List;

public interface BasicDomainSet {

  List<BasicDomain> getByDomainType(BasicDomainPurpose type);

  BasicDomain getByDomainName(String domainName);

  BasicDomain getByDelegateClassName(String delegateClassName);

  boolean isDomainDomain(String domainName);
}
