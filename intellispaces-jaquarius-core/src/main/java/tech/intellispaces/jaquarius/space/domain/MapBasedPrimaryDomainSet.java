package tech.intellispaces.jaquarius.space.domain;

import tech.intellispaces.general.exception.UnexpectedExceptions;

import java.util.List;
import java.util.Map;

class MapBasedPrimaryDomainSet implements PrimaryDomainSet {
  private final Map<PrimaryDomainType, List<PrimaryDomain>> map;

  MapBasedPrimaryDomainSet(Map<PrimaryDomainType, List<PrimaryDomain>> map) {
    this.map = map;
  }

  @Override
  public List<PrimaryDomain> get(PrimaryDomainType type) {
    List<PrimaryDomain> list = map.get(type);
    if (list == null) {
      throw UnexpectedExceptions.withMessage("Unable to get core domain by type {0}", type.name());
    }
    return list;
  }

  @Override
  public boolean isDomainDomain(String domainName) {
    return get(PrimaryDomainTypes.Domain).stream()
        .anyMatch(c -> c.domainName().equals(domainName));
  }
}
