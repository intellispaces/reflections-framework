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
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;

public class SystemReflectionImpl implements SystemReflection {
  private final Rid rid;
  private final Reflection wrappedReflection;
  private final OntologyRepository ontologyRepository;

  public SystemReflectionImpl(Reflection wrappedReflection, OntologyRepository ontologyRepository) {
    this(wrappedReflection.rid(), wrappedReflection, ontologyRepository);
  }

  public SystemReflectionImpl(
      Rid rid,
      Reflection wrappedReflection,
      OntologyRepository ontologyRepository
  ) {
    this.rid = rid;
    this.wrappedReflection = wrappedReflection;
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
    return wrappedReflection.reflectionName();
  }

  @Override
  public Projection projectionThru(Rid cid) {
    Projection projection = wrappedReflection.projectionThru(cid);
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
    Projection projection = wrappedReflection.projectionThru(channelName);
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
    Projection projection = wrappedReflection.projectionTo(domainName);
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
  public boolean canBeRepresentedAsPoint() {
    return wrappedReflection.canBeRepresentedAsPoint();
  }

  @Override
  public boolean canBeRepresentedAsDomain() {
    return wrappedReflection.canBeRepresentedAsDomain();
  }

  @Override
  public boolean canBeRepresentedAsChannel() {
    return wrappedReflection.canBeRepresentedAsChannel();
  }

  @Override
  public boolean canBeRepresentedAsSpace() {
    return wrappedReflection.canBeRepresentedAsSpace();
  }

  @Override
  public ReflectionPoint asPoint() {
    if (wrappedReflection.canBeRepresentedAsPoint()) {
      return new SystemReflectionPointImpl(wrappedReflection.asPoint(), ontologyRepository);
    }
    if (rid != null) {
      Reflection reflection = ontologyRepository.findReflection(rid);
      if (reflection != null && reflection.canBeRepresentedAsPoint()) {
        return reflection.asPoint();
      }
    }
    if (wrappedReflection.reflectionName() != null) {
      Reflection reflection = ontologyRepository.findReflection(wrappedReflection.reflectionName());
      if (reflection != null) {
        if (reflection.canBeRepresentedAsPoint()) {
          return reflection.asPoint();
        } else if (reflection.canBeRepresentedAsDomain()) {
          return new SystemReflectionDomainImpl(reflection.asDomain(), ontologyRepository).asPoint();
        } else if (reflection.canBeRepresentedAsSpace()) {
          return new SystemReflectionSpaceImpl(reflection.asSpace(), ontologyRepository).asPoint();
        }
      }
    }
    throw NotImplementedExceptions.withCode("vq5z2A");
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
