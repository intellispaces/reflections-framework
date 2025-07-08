package tech.intellispaces.reflections.framework.system;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.core.Channels;
import tech.intellispaces.core.OntologyRepository;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionChannel;
import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;
import tech.intellispaces.core.ReflectionSpace;
import tech.intellispaces.core.Rid;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.customtype.CustomTypes;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;

public class LocalClassPathSpaceRepository implements OntologyRepository {
  private final String spaceName;
  private final String classNamePrefix;

  public LocalClassPathSpaceRepository(String spaceName, String classNamePrefix) {
    this.spaceName = spaceName;
    this.classNamePrefix = classNamePrefix;
  }

  @Override
  public Collection<String> spaces() {
    return List.of(spaceName);
  }

  @Override
  public boolean add(Reflection reflection) {
    return false;
  }

  @Override
  public @Nullable Reflection findReflection(String reflectionName) {
    return null;
  }

  @Override
  public @Nullable ReflectionPoint findReflection(Rid pid, String domainName) {
    return null;
  }

  @Override
  public @Nullable ReflectionSpace findSpace(String spaceName) {
    throw NotImplementedExceptions.withCode("hiUBxA");
  }

  @Override
  public ReflectionDomain findDomain(String domainName) {
    Optional<Class<?>> domainClass = ClassFunctions.getClass(classNamePrefix + domainName + "Domain");
    return domainClass.map(DomainFunctions::getDomain).orElse(null);
  }

  @Override
  public List<ReflectionDomain> findSubdomains(Rid did) {
    return null;
  }

  @Override
  public List<ReflectionDomain> findSubdomains(String domainName) {
    return null;
  }

  @Override
  public @Nullable ReflectionDomain findBorrowedDomain(String domainName) {
    return null;
  }

  @Override
  public ReflectionChannel findChannel(String channelName) {
    throw NotImplementedExceptions.withCode("x9Q6gw");
  }

  @Override
  public @Nullable ReflectionChannel findChannel(ReflectionDomain sourceDomain, ReflectionDomain targetDomain) {
    Class<?> sourceDomainClass = getDomainClass(sourceDomain);
    if (sourceDomainClass == null) {
      return null;
    }
    CustomType sourceDomainType = CustomTypes.of(sourceDomainClass);
    Rid cid = DomainFunctions.findChannel(sourceDomainType, targetDomain.rid());
    if (cid == null) {
      return null;
    }
    return Channels.build()
        .cid(cid)
        .sourceDomain(sourceDomain)
        .targetDomain(targetDomain)
        .get();
  }

  @Override
  public List<ReflectionChannel> findDomainChannels(String domainName) {
    return List.of();
  }

  @Override
  public List<Reflection> findRelatedReflections(String reflectionName) {
    throw NotImplementedExceptions.withCode("ahh0OpW7");
  }

  private @Nullable Class<?> getDomainClass(ReflectionDomain domain) {
    if (domain.domainClass() != null) {
      return domain.domainClass();
    }
    if (domain.reflectionName() != null) {
      Optional<Class<?>> domainClass = ClassFunctions.getClass(classNamePrefix + domain.reflectionName() + "Domain");
      return domainClass.orElse(null);
    }
    throw NotImplementedExceptions.withCode("a6vc/A");
  }
}
