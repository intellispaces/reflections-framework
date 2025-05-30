package tech.intellispaces.reflections.framework.system;

import java.util.Optional;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.core.Channel;
import tech.intellispaces.core.Domain;
import tech.intellispaces.core.repository.SpaceRepository;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.customtype.CustomTypes;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;

public class LocalClassPathSpaceRepository implements SpaceRepository {

  @Override
  public Domain findDomain(String name) {
    throw NotImplementedExceptions.withCode("iGEHvQ");
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
      Optional<Class<?>> domainClass = ClassFunctions.getClass(domain.name());
      return domainClass.orElse(null);
    }
    throw NotImplementedExceptions.withCode("a6vc/A");
  }
}
