package tech.intellispaces.reflections.framework.system;

import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.core.OntologyRepository;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionChannel;
import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;
import tech.intellispaces.core.ReflectionSpace;
import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.system.remote.RemoteController;

public class RemoteOntologyRepository implements OntologyRepository {
  private final RemoteController remoteController;

  public RemoteOntologyRepository(RemoteController remoteController) {
    this.remoteController = remoteController;
  }

  @Override
  public Collection<String> spaces() {
    return remoteController.getSpaces();
  }

  @Override
  public boolean add(Reflection reflection) {
    return false;
  }

  @Override
  public @Nullable Reflection findReflection(String reflectionName) {
    throw NotImplementedExceptions.withCode("c7fQVd43");
  }

  @Override
  public @Nullable ReflectionPoint findReflection(Rid pid, String domainName) {
    if (!remoteController.checkContainReflection(pid, domainName)) {
      return null;
    }
    throw NotImplementedExceptions.withCode("q3mhUYWc");
  }

  @Override
  public @Nullable ReflectionSpace findSpace(String spaceName) {
    throw NotImplementedExceptions.withCode("ZNMqSAoN");
  }

  @Override
  public @Nullable ReflectionDomain findDomain(String domainName) {
    throw NotImplementedExceptions.withCode("yvQcDJJa");
  }

  @Override
  public @Nullable List<ReflectionDomain> findSubdomains(Rid did) {
    throw NotImplementedExceptions.withCode("hEphOXYq");
  }

  @Override
  public @Nullable List<ReflectionDomain> findSubdomains(String domainName) {
    throw NotImplementedExceptions.withCode("q3dQZ4ju");
  }

  @Override
  public @Nullable ReflectionDomain findBorrowedDomain(String domainName) {
    throw NotImplementedExceptions.withCode("enfJaObf");
  }

  @Override
  public @Nullable ReflectionChannel findChannel(String channelName) {
    throw NotImplementedExceptions.withCode("VtgSeIYU");
  }

  @Override
  public @Nullable ReflectionChannel findChannel(ReflectionDomain sourceDomain, ReflectionDomain targetDomain) {
    throw NotImplementedExceptions.withCode("7dKJHasl");
  }

  @Override
  public List<ReflectionChannel> findDomainChannels(String domainName) {
    throw NotImplementedExceptions.withCode("4ovHLbAh");
  }

  @Override
  public List<Reflection> findRelatedReflections(String reflectionName) {
    throw NotImplementedExceptions.withCode("TG4jgT2P");
  }
}
