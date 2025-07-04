package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.core.OntologyRepository;
import tech.intellispaces.core.Projection;
import tech.intellispaces.core.Projections;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionChannel;
import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;
import tech.intellispaces.core.ReflectionSpace;
import tech.intellispaces.core.Rid;
import tech.intellispaces.core.TraversableReflectionPoint;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;

public class SystemReflectionPointImpl implements TraversableReflectionPoint, SystemReflection {
  private final Rid rid;
  private final ReflectionPoint wrappedPoint;
  private final OntologyRepository ontologyRepository;

  public SystemReflectionPointImpl(ReflectionPoint wrappedPoint, OntologyRepository ontologyRepository) {
    this(wrappedPoint.rid(), wrappedPoint, ontologyRepository);
  }

  public SystemReflectionPointImpl(
      Rid rid,
      ReflectionPoint wrappedPoint,
      OntologyRepository ontologyRepository
  ) {
    this.rid = rid;
    this.wrappedPoint = wrappedPoint;
    this.ontologyRepository = ontologyRepository;
  }

  @Override
  public <TD, T> T mapTo(Class<TD> targetDomain) {
    throw NotImplementedExceptions.withCode("Tas6DWba");
  }

  @Override
  public <T, Q, C extends Channel1 & MappingTraverse> T mapThru(
      Class<C> channelClass, Q qualifier
  ) throws TraverseException {
    throw NotImplementedExceptions.withCode("MFGS6rRZ");
  }

  @Override
  public void unbind() {
    throw NotImplementedExceptions.withCode("CIIdUU3f");
  }

  @Override
  public MovableReflection asMovableOrElseThrow() {
    throw NotImplementedExceptions.withCode("sB6TpGH5");
  }

  @Override
  public <TD, T> void addProjection(Class<TD> targetDomain, T target) {
    throw NotImplementedExceptions.withCode("kxGr9MFz");
  }

  @Override
  public List<? extends SystemReflection> underlyingReflections() {
    throw NotImplementedExceptions.withCode("m3K7D97z");
  }

  @Override
  public SystemReflection overlyingReflection() {
    throw NotImplementedExceptions.withCode("IwQvqeop");
  }

  @Override
  public @Nullable Rid rid() {
    return rid;
  }

  @Override
  public @Nullable String reflectionName() {
    return wrappedPoint.reflectionName();
  }

  @Override
  public ReflectionDomain domain() {
    ReflectionDomain domain = wrappedPoint.domain();
    if (domain == null) {
      Reflection reflection = ontologyRepository.findReflection(reflectionName());
      if (reflection != null) {
        domain = reflection.asPoint().domain();
      }
    }
    if (domain == null) {
      return null;
    }
    return new SystemReflectionDomainImpl(domain, ontologyRepository);
  }

  @Override
  public Projection projectionThru(Rid cid) {
    Projection projection = wrappedPoint.projectionThru(cid);
    if (!projection.isUnknown()) {
      return projection;
    }
    if (rid() != null) {
      Reflection reflection = ontologyRepository.findReflection(rid());
      if (reflection != null) {
        return reflection.projectionThru(cid);
      }
    }
    if (reflectionName() != null) {
      Reflection reflection = ontologyRepository.findReflection(reflectionName());
      if (reflection != null) {
        return reflection.projectionThru(cid);
      }
    }
    return Projections.unknown();
  }

  @Override
  public Projection projectionThru(String channelName) {
    Projection projection = wrappedPoint.projectionThru(channelName);
    if (!projection.isUnknown()) {
      return projection;
    }
    if (rid() != null) {
      Reflection reflection = ontologyRepository.findReflection(rid());
      if (reflection != null) {
        return reflection.projectionThru(channelName);
      }
    }
    if (reflectionName() != null) {
      Reflection reflection = ontologyRepository.findReflection(reflectionName());
      if (reflection != null) {
        return reflection.projectionThru(channelName);
      }
    }
    return Projections.unknown();
  }

  @Override
  public Projection projectionTo(String domainName) {
    Projection projection = wrappedPoint.projectionTo(domainName);
    if (!projection.isUnknown()) {
      return projection;
    }
    if (rid() != null) {
      Reflection reflection = ontologyRepository.findReflection(rid());
      if (reflection != null) {
        return reflection.projectionTo(domainName);
      }
    }
    if (reflectionName() != null) {
      Reflection reflection = ontologyRepository.findReflection(reflectionName());
      if (reflection != null) {
        return reflection.projectionTo(domainName);
      }
    }
    return Projections.unknown();
  }

  @Override
  public List<Reflection> relatedReflections() {
    return ontologyRepository.findRelatedReflections(reflectionName());
  }

  @Override
  public List<ReflectionPoint> underlyingPoints() {
    return wrappedPoint.underlyingPoints();
  }

  @Override
  public boolean canBeRepresentedAsPoint() {
    return true;
  }

  @Override
  public boolean canBeRepresentedAsDomain() {
    throw NotImplementedExceptions.withCode("YmzViJQr");
  }

  @Override
  public boolean canBeRepresentedAsChannel() {
    throw NotImplementedExceptions.withCode("o4dBTJGI");
  }

  @Override
  public boolean canBeRepresentedAsSpace() {
    throw NotImplementedExceptions.withCode("F9akXxA7");
  }

  @Override
  public ReflectionPoint asPoint() {
    return this;
  }

  @Override
  public ReflectionDomain asDomain() {
    throw NotImplementedExceptions.withCode("yOyWDcMh");
  }

  @Override
  public ReflectionChannel asChannel() {
    throw NotImplementedExceptions.withCode("wFS2J1Tr");
  }

  @Override
  public ReflectionSpace asSpace() {
    throw NotImplementedExceptions.withCode("B6aXIhQc");
  }
}
