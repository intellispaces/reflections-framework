package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.core.Domains;
import tech.intellispaces.core.OntologyRepository;
import tech.intellispaces.core.Projection;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionChannel;
import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;
import tech.intellispaces.core.ReflectionSpace;
import tech.intellispaces.core.Rid;
import tech.intellispaces.core.Rids;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;

public class SystemReflectionSpaceImpl implements ReflectionSpace, ReflectionPoint, SystemReflection {
  private final ReflectionSpace wrappedSpace;
  private final OntologyRepository ontologyRepository;

  public SystemReflectionSpaceImpl(ReflectionSpace wrappedSpace, OntologyRepository ontologyRepository) {
    this.wrappedSpace = wrappedSpace;
    this.ontologyRepository = ontologyRepository;
  }

  @Override
  public ReflectionDomain domain() {
//    DomainReference domainReference = ReflectionsNodeFunctions.ontologyReference().getDomainByType(
//        DomainAssignments.Space
//    );
    return new SystemReflectionDomainImpl(
        Domains.build()
            .did(Rids.fromUuid("c0e2eafe-3e82-4aff-b35d-4555d63636cd"))
            .name("ixora.basic.Space")
            .get(),
        ontologyRepository
    );
  }

  @Override
  public List<ReflectionPoint> underlyingPoints() {
    return List.of();
  }

  @Override
  public @Nullable Rid rid() {
    return wrappedSpace.rid();
  }

  @Override
  public @Nullable String reflectionName() {
    return wrappedSpace.reflectionName();
  }

  @Override
  public Projection projectionThru(Rid cid) {
    return wrappedSpace.projectionThru(cid);
  }

  @Override
  public Projection projectionThru(String channelName) {
    return wrappedSpace.projectionThru(channelName);
  }

  @Override
  public Projection projectionTo(String domainName) {
    return wrappedSpace.projectionTo(domainName);
  }

  @Override
  public List<Reflection> relatedReflections() {
    List<Reflection> reflections = wrappedSpace.relatedReflections();
    if (reflections.isEmpty()) {
      reflections = ontologyRepository.findRelatedReflections(wrappedSpace.reflectionName());
    }
    return reflections;
  }

  @Override
  public boolean canBeRepresentedAsPoint() {
    return true;
  }

  @Override
  public boolean canBeRepresentedAsDomain() {
    return false;
  }

  @Override
  public boolean canBeRepresentedAsChannel() {
    return false;
  }

  @Override
  public boolean canBeRepresentedAsSpace() {
    return true;
  }

  @Override
  public ReflectionPoint asPoint() {
    return this;
  }

  @Override
  public ReflectionDomain asDomain() {
    throw UnexpectedExceptions.withMessage("This reflection cannot be considered as a reflection domain");
  }

  @Override
  public ReflectionChannel asChannel() {
    throw UnexpectedExceptions.withMessage("This reflection cannot be considered as a reflection channel");
  }

  @Override
  public ReflectionSpace asSpace() {
    return this;
  }

  @Override
  public <TD, T> T mapTo(Class<TD> targetDomain) {
    throw NotImplementedExceptions.withCode("5YvGxg");
  }

  @Override
  public <T, Q, C extends Channel1 & MappingTraverse> T mapThru(
      Class<C> channelClass, Q qualifier
  ) throws TraverseException {
    throw NotImplementedExceptions.withCode("WefMow");
  }

  @Override
  public void unbind() {
    throw NotImplementedExceptions.withCode("FNz5Zg");
  }

  @Override
  public MovableReflection asMovableOrElseThrow() {
    throw NotImplementedExceptions.withCode("LmBVkA");
  }

  @Override
  public <TD, T> void addProjection(Class<TD> targetDomain, T target) {
    throw NotImplementedExceptions.withCode("pUGycA");
  }

  @Override
  public List<? extends SystemReflection> underlyingReflections() {
    throw NotImplementedExceptions.withCode("GZnN3A");
  }

  @Override
  public SystemReflection overlyingReflection() {
    throw NotImplementedExceptions.withCode("KG8Ffw");
  }
}
