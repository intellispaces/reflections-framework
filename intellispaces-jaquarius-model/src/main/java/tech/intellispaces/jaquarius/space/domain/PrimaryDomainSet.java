package tech.intellispaces.jaquarius.space.domain;

import java.util.List;

public interface PrimaryDomainSet {

  List<PrimaryDomain> get(PrimaryDomainType type);

  boolean isDomainDomain(String domainName);
}
