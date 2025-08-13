package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
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
  public List<? extends SystemReflection> parentReflections() {
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
  public @Nullable String alias() {
    return wrappedPoint.alias();
  }

  @Override
  public ReflectionDomain domain() {
    ReflectionDomain domain = wrappedPoint.domain();
    if (domain == null) {
      Reflection reflection = ontologyRepository.findReflection(alias());
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
  public @Nullable String domainAlias() {
    ReflectionDomain domain = domain();
    if (domain == null) {
      return null;
    }
    return domain.alias();
  }

  @Override
  public Projection projectionThru(Rid cid) {
    Projection projection = wrappedPoint.projectionThru(cid);
    if (!projection.isUnknown()) {
      return projection;
    }
    projection = ontologyRepository.findProjection(rid(), domain().did(), cid);
    if (!projection.isUnknown()) {
      return projection;
    }

    if (rid() != null && domain() != null) {
      if (domain().did() != null) {
        Reflection reflection = ontologyRepository.findReflection(rid(), domain().did());
        if (reflection != null) {
          return reflection.projectionThru(cid);
        }
      }
      if (domain().alias() != null) {
        Reflection reflection = ontologyRepository.findReflection(rid(), domain().alias());
        if (reflection != null) {
          return reflection.projectionThru(cid);
        }
      }
    }
    if (alias() != null) {
      Reflection reflection = ontologyRepository.findReflection(alias());
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
    if (rid() != null && domainAlias() != null) {
      Reflection reflection = ontologyRepository.findReflection(rid(), domainAlias());
      if (reflection != null) {
        return reflection.projectionThru(channelName);
      }
    }
    if (alias() != null) {
      Reflection reflection = ontologyRepository.findReflection(alias());
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
    if (rid() != null && domainAlias() != null) {
      Reflection reflection = ontologyRepository.findReflection(rid(), domainAlias());
      if (reflection != null) {
        return reflection.projectionTo(domainName);
      }
    }
    if (alias() != null) {
      Reflection reflection = ontologyRepository.findReflection(alias());
      if (reflection != null) {
        return reflection.projectionTo(domainName);
      }
    }
    return Projections.unknown();
  }

  @Override
  public Projection projectionTo(ReflectionDomain domain) {
    Projection projection = wrappedPoint.projectionTo(domain);
    if (!projection.isUnknown()) {
      return projection;
    }
    if (rid() != null && domainAlias() != null) {
      Reflection reflection = ontologyRepository.findReflection(rid(), domainAlias());
      if (reflection != null) {
        return reflection.projectionTo(domain);
      }
    }
    if (alias() != null) {
      Reflection reflection = ontologyRepository.findReflection(alias());
      if (reflection != null) {
        return reflection.projectionTo(domain);
      }
    }
    return Projections.unknown();
  }

  @Override
  public List<Reflection> relatedReflections() {
    return ontologyRepository.findRelatedReflections(rid(), domain().did());
  }

  @Override
  public List<ReflectionPoint> parentPoints() {
    return wrappedPoint.parentPoints();
  }

  @Override
  public boolean canBeRepresentedAsPoint() {
    return true;
  }

  @Override
  public boolean canBeRepresentedAsDomain() {
    return wrappedPoint.canBeRepresentedAsDomain();
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
    if (canBeRepresentedAsDomain()) {
      return new SystemReflectionDomainImpl(wrappedPoint.asDomain(), ontologyRepository);
    }
    throw UnexpectedExceptions.withMessage("This reflection cannot be casted to domain");
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
