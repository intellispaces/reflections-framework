package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.core.Domains;
import tech.intellispaces.core.OntologyRepository;
import tech.intellispaces.core.Projection;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionChannel;
import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;
import tech.intellispaces.core.ReflectionSpace;
import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.node.ReflectionsNodeFunctions;
import tech.intellispaces.reflections.framework.settings.DomainAssignments;
import tech.intellispaces.reflections.framework.settings.DomainReference;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;

public class SystemReflectionDomainImpl implements ReflectionDomain, ReflectionPoint, SystemReflection {
  private final ReflectionDomain wrappedDomain;
  private final OntologyRepository ontologyRepository;

  public SystemReflectionDomainImpl(ReflectionDomain wrappedDomain, OntologyRepository ontologyRepository) {
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
  public List<ReflectionDomain> foreignDomains() {
    List<ReflectionDomain> foreignDomains = wrappedDomain.foreignDomains();
    if (foreignDomains == null || foreignDomains.isEmpty()) {
      foreignDomains = ontologyRepository.findForeignDomains(rid());
    }
    return foreignDomains != null ? foreignDomains : List.of();
  }

  @Override
  public List<ReflectionChannel> domainChannels() {
    return ontologyRepository.findDomainContextChannels(reflectionName());
  }

  @Override
  public List<ReflectionDomain> parentDomains() {
    List<ReflectionDomain> domains = wrappedDomain.parentDomains();
    if (domains != null && !domains.isEmpty()) {
      return domains.stream()
          .map(domain -> (ReflectionDomain) new SystemReflectionDomainImpl(domain, ontologyRepository))
          .toList();
    }
    DomainReference notionDomainReference = ReflectionsNodeFunctions.ontologyReference().getDomainByType(
        DomainAssignments.Notion
    );
    ReflectionDomain notionDomain = Domains.build()
        .did(notionDomainReference.domainId())
        .name(notionDomainReference.domainAlias())
        .get();
    return List.of(new SystemReflectionDomainImpl(notionDomain, ontologyRepository));
  }

  @Override
  public ReflectionDomain domain() {
    DomainReference domainReference = ReflectionsNodeFunctions.ontologyReference().getDomainByType(
        DomainAssignments.Domain
    );
    return new SystemReflectionDomainImpl(ontologyRepository.findDomain(domainReference.domainAlias()), ontologyRepository);
  }

  @Override
  public @Nullable String domainName() {
    return domain().reflectionName();
  }

  @Override
  public List<ReflectionPoint> underlyingPoints() {
    return List.of();
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
    Projection projection = wrappedDomain.projectionThru(cid);
    if (!projection.isUnknown()) {
      return projection;
    }
    return ontologyRepository.findProjection(rid(), domain().did(), cid);
  }

  @Override
  public Projection projectionThru(String channelName) {
    return wrappedDomain.projectionThru(channelName);
  }

  @Override
  public Projection projectionTo(String domainName) {
    return wrappedDomain.projectionTo(domainName);
  }

  @Override
  public Projection projectionTo(ReflectionDomain domain) {
    return wrappedDomain.projectionTo(domain);
  }

  @Override
  public List<Reflection> relatedReflections() {
    List<Reflection> reflections = wrappedDomain.relatedReflections();
    if (reflections.isEmpty()) {
      reflections = ontologyRepository.findRelatedReflections(wrappedDomain.reflectionName());
    }
    return reflections;
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
  public <TD, T> T mapTo(Class<TD> targetDomain) {
    throw NotImplementedExceptions.withCode("E2TSiA");
  }

  @Override
  public <T, Q, C extends Channel1 & MappingTraverse> T mapThru(
      Class<C> channelClass, Q qualifier
  ) throws TraverseException {
    throw NotImplementedExceptions.withCode("HOAytg");
  }

  @Override
  public void unbind() {
    throw NotImplementedExceptions.withCode("DQ6MBA");
  }

  @Override
  public MovableReflection asMovableOrElseThrow() {
    throw NotImplementedExceptions.withCode("OOaHCQ");
  }

  @Override
  public <TD, T> void addProjection(Class<TD> targetDomain, T target) {
    throw NotImplementedExceptions.withCode("uhotWA");
  }

  @Override
  public List<? extends SystemReflection> underlyingReflections() {
    throw NotImplementedExceptions.withCode("3BVBXA");
  }

  @Override
  public SystemReflection overlyingReflection() {
    throw NotImplementedExceptions.withCode("kkZjGQ");
  }

  @Override
  public String toString() {
    String domainName = reflectionName();
    Rid did = did();
    if (domainName != null) {
      return domainName + (did != null ? "(did = " + did + ")" : "");
    }
    if (did != null) {
      return did.toString();
    }
    return super.toString();
  }
}
