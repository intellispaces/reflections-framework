package tech.intellispaces.reflections.framework.system;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.core.Channel;
import tech.intellispaces.core.Domain;
import tech.intellispaces.core.OntologyRepository;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.Space;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.customtype.CustomTypes;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;

public class LocalClassPathSpaceRepository implements OntologyRepository {
  private final String prefix;

  public LocalClassPathSpaceRepository(String prefix) {
    this.prefix = prefix;
  }

  @Override
  public @Nullable Reflection findReflection(String reflectionName) {
    throw NotImplementedExceptions.withCode("q8HBoQhh");
  }

  @Override
  public @Nullable Space findSpace(String spaceName) {
    throw NotImplementedExceptions.withCode("hiUBxA");
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

  @Override
  public List<Channel> projectionChannels(String domainName) {
    throw NotImplementedExceptions.withCode("we7j3Jqx");
  }

  @Override
  public List<Reflection> findRelatedReflections(String reflectionName) {
    throw NotImplementedExceptions.withCode("ahh0OpW7");
  }
}
