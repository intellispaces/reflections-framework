package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.core.OntologyRepository;
import tech.intellispaces.core.Projection;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionChannel;
import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;
import tech.intellispaces.core.ReflectionSpace;
import tech.intellispaces.core.Rid;

public class SystemDomainImpl implements ReflectionDomain {
  private final ReflectionDomain wrappedDomain;
  private final OntologyRepository ontologyRepository;

  public SystemDomainImpl(ReflectionDomain wrappedDomain, OntologyRepository ontologyRepository) {
    this.wrappedDomain = wrappedDomain;
    this.ontologyRepository = ontologyRepository;
  }

  @Override
  public @Nullable Class<?> domainClass() {
    return wrappedDomain.domainClass();
  }

  @Override
  public @Nullable Type<?> domainType() {
    return wrappedDomain.domainType();
  }

  @Override
  public @Nullable ReflectionDomain borrowedDomain() {
    return wrappedDomain.borrowedDomain();
  }

  @Override
  public List<ReflectionChannel> domainChannels() {
    return ontologyRepository.findDomainChannels(reflectionName());
  }

  @Override
  public @Nullable Rid rid() {
    return wrappedDomain.rid();
  }

  @Override
  public @Nullable String reflectionName() {
    return wrappedDomain.reflectionName();
  }

  @Override
  public Projection projectionThru(Rid cid) {
    return wrappedDomain.projectionThru(cid);
  }

  @Override
  public Projection projectionThru(String channelName) {
    return wrappedDomain.projectionThru(channelName);
  }

  @Override
  public Projection projectionTo(String domainName) {
    return projectionThru(domainName);
  }

  @Override
  public List<Reflection> relatedReflections() {
    return wrappedDomain.relatedReflections();
  }

  @Override
  public boolean canBeRepresentedAsPoint() {
    return true;
  }

  @Override
  public boolean canBeRepresentedAsDomain() {
    return true;
  }

  @Override
  public boolean canBeRepresentedAsChannel() {
    return false;
  }

  @Override
  public boolean canBeRepresentedAsSpace() {
    return false;
  }

  @Override
  public ReflectionPoint asPoint() {
    throw NotImplementedExceptions.withCode("TI9ZT6cs");
  }

  @Override
  public ReflectionDomain asDomain() {
    return this;
  }

  @Override
  public ReflectionChannel asChannel() {
    throw UnexpectedExceptions.withMessage("This reflection cannot be considered as a reflection channel");
  }

  @Override
  public ReflectionSpace asSpace() {
    throw UnexpectedExceptions.withMessage("This reflection cannot be considered as a reflection space");
  }
}
