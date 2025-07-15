package tech.intellispaces.reflections.framework.space.domain;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.commons.type.Types;
import tech.intellispaces.core.Projection;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionChannel;
import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;
import tech.intellispaces.core.ReflectionSpace;
import tech.intellispaces.core.Rid;

public class ClassReflectionDomain implements ReflectionDomain, ReflectionPoint {
  private final Class<?> domainClass;

  public ClassReflectionDomain(Class<?> domainClass) {
    this.domainClass = domainClass;
  }

  @Override
  public @Nullable Class<?> domainClass() {
    return domainClass;
  }

  @Override
  public @Nullable Type<?> domainType() {
    return Types.get(domainClass);
  }

  @Override
  public @Nullable ReflectionDomain borrowedDomain() {
    return null;
  }

  @Override
  public List<ReflectionChannel> domainChannels() {
    return DomainFunctions.getDomainChannels(domainClass);
  }

  @Override
  public List<ReflectionDomain> parentDomains() {
    return DomainFunctions.getParentDomains(domainClass);
  }

  @Override
  public ReflectionDomain domain() {
    throw NotImplementedExceptions.withCode("SdAfKHRX");
  }

  @Override
  public @Nullable String domainName() {
    return domain().reflectionName();
  }

  @Override
  public List<ReflectionPoint> underlyingPoints() {
    throw NotImplementedExceptions.withCode("XfL38InR");
  }

  @Override
  public Projection projectionThru(Rid cid) {
    throw NotImplementedExceptions.withCode("pJyyMpLr");
  }

  @Override
  public Projection projectionThru(String channelName) {
    throw NotImplementedExceptions.withCode("kGBykbqX");
  }

  @Override
  public Projection projectionTo(String domainName) {
    throw NotImplementedExceptions.withCode("FHW0qTrZ");
  }

  @Override
  public Projection projectionTo(ReflectionDomain domain) {
    throw NotImplementedExceptions.withCode("ZUIbKCwT");
  }

  @Override
  public List<Reflection> relatedReflections() {
    return List.of();
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
    return this;
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

  @Override
  public @Nullable Rid rid() {
    return DomainFunctions.getDomainId(domainClass);
  }

  @Override
  public @Nullable String reflectionName() {
    return DomainFunctions.getDomainName(domainClass);
  }
}
