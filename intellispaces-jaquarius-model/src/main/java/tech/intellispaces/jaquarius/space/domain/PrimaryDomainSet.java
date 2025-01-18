package tech.intellispaces.jaquarius.space.domain;

import java.util.List;

public interface PrimaryDomainSet {

  List<PrimaryDomain> getByDomainType(PrimaryDomainType type);

  PrimaryDomain getByDomainName(String domainName);

  boolean isDomainDomain(String domainName);
}
