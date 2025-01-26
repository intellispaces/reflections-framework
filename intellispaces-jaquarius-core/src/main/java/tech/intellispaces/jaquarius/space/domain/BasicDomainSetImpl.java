package tech.intellispaces.jaquarius.space.domain;

import tech.intellispaces.general.exception.UnexpectedExceptions;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class BasicDomainSetImpl implements BasicDomainSet {
  private final Map<BasicDomainPurpose, List<BasicDomain>> indexByType;
  private final Map<String, BasicDomain> indexByDomainName;
  private final Map<String, BasicDomain> indexByDelegateClassName;

  BasicDomainSetImpl(List<BasicDomain> basicDomains) {
    this.indexByType = basicDomains.stream().collect(Collectors.groupingBy(BasicDomain::purpose));
    this.indexByDomainName = basicDomains.stream().collect(Collectors.toMap(
        BasicDomain::domainName, Function.identity())
    );
    this.indexByDelegateClassName = basicDomains.stream().collect(Collectors.toMap(
        BasicDomain::delegateClassName, Function.identity())
    );
  }

  @Override
  public List<BasicDomain> getByDomainType(BasicDomainPurpose type) {
    List<BasicDomain> list = indexByType.get(type);
    if (list == null) {
      throw UnexpectedExceptions.withMessage("Unable to get core domain by type {0}", type.name());
    }
    return list;
  }

  @Override
  public BasicDomain getByDomainName(String domainName) {
    return indexByDomainName.get(domainName);
  }

  @Override
  public BasicDomain getByDelegateClassName(String delegateClassName) {
    return indexByDelegateClassName.get(delegateClassName);
  }

  @Override
  public boolean isDomainDomain(String domainName) {
    return getByDomainType(BasicDomainPurposes.Domain).stream()
        .anyMatch(c -> c.domainName().equals(domainName));
  }
}
