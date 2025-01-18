package tech.intellispaces.jaquarius.space.domain;

import tech.intellispaces.general.exception.UnexpectedExceptions;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class PrimaryDomainSetImpl implements PrimaryDomainSet {
  private final Map<PrimaryDomainType, List<PrimaryDomain>> indexByType;
  private final Map<String, PrimaryDomain> indexByDomainName;

  PrimaryDomainSetImpl(List<PrimaryDomain> primaryDomains) {
    this.indexByType = primaryDomains.stream().collect(Collectors.groupingBy(PrimaryDomain::type));
    this.indexByDomainName = primaryDomains.stream().collect(Collectors.toMap(
        PrimaryDomain::domainName, Function.identity())
    );
  }

  @Override
  public List<PrimaryDomain> getByDomainType(PrimaryDomainType type) {
    List<PrimaryDomain> list = indexByType.get(type);
    if (list == null) {
      throw UnexpectedExceptions.withMessage("Unable to get core domain by type {0}", type.name());
    }
    return list;
  }

  @Override
  public PrimaryDomain getByDomainName(String domainName) {
    return indexByDomainName.get(domainName);
  }

  @Override
  public boolean isDomainDomain(String domainName) {
    return getByDomainType(PrimaryDomainTypes.Domain).stream()
        .anyMatch(c -> c.domainName().equals(domainName));
  }
}
