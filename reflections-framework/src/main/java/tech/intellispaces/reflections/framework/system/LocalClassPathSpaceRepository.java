package tech.intellispaces.reflections.framework.system;

import java.util.Optional;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.core.Channel;
import tech.intellispaces.core.Domain;
import tech.intellispaces.core.repository.OntologyRepository;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.customtype.CustomTypes;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;

public class LocalClassPathSpaceRepository implements OntologyRepository {
  private final String prefix;

  public LocalClassPathSpaceRepository(String prefix) {
    this.prefix = prefix;
  }

  @Override
  public Domain findDomain(String name) {
    Optional<Class<?>> domainClass = ClassFunctions.getClass(prefix + name + "Domain");
    return domainClass.map(DomainFunctions::getDomain).orElse(null);
  }

  @Override
  public Channel findChannel(String name) {
    throw NotImplementedExceptions.withCode("x9Q6gw");
  }

  @Override
  public Channel findChannel(Domain sourceDomain, Domain targetDomain) {
    Class<?> sourceDomainClass = getDomainClass(sourceDomain);
    CustomType sourceDomainType = CustomTypes.of(sourceDomainClass);
    return DomainFunctions.findChannel(sourceDomainType, targetDomain.rid());
  }

  private Class<?> getDomainClass(Domain domain) {
    if (domain.domainClass() != null) {
      return domain.domainClass();
    }
    if (domain.name() != null) {
      Optional<Class<?>> domainClass = ClassFunctions.getClass(prefix + domain.name() + "Domain");
      return domainClass.orElse(null);
    }
    throw NotImplementedExceptions.withCode("a6vc/A");
  }
}
