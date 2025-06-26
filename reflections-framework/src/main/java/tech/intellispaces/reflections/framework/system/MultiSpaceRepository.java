package tech.intellispaces.reflections.framework.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.core.Channel;
import tech.intellispaces.core.Domain;
import tech.intellispaces.core.OntologyRepository;
import tech.intellispaces.core.Point;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.Rid;
import tech.intellispaces.core.Space;
import tech.intellispaces.core.SpaceFunctions;

public class MultiSpaceRepository implements OntologyRepository {
  private final Map<String, OntologyRepository> repositories = new HashMap<>();

  public void addRepository(String spaceName, OntologyRepository repository) {
    repositories.put(spaceName, repository);
  }

  @Override
  public @Nullable Reflection findReflection(Rid rid) {
    for (OntologyRepository repository : repositories.values()) {
      Reflection reflection = repository.findReflection(rid);
      if (reflection != null) {
        return reflection;
      }
    }
    return null;
  }

  @Override
  public @Nullable Reflection findReflection(String reflectionName) {
    OntologyRepository repository = selectRepository(reflectionName);
    if (repository == null) {
      return null;
    }
    return repository.findReflection(reflectionName);
  }

  @Override
  public @Nullable Space findSpace(String spaceName) {
    OntologyRepository repository = selectRepository(spaceName);
    if (repository == null) {
      return null;
    }
    return repository.findSpace(spaceName);
  }

  @Override
  public @Nullable Domain findDomain(String domainName) {
    OntologyRepository repository = selectRepository(domainName);
    if (repository == null) {
      return null;
    }
    return repository.findDomain(domainName);
  }

  @Override
  public @Nullable Channel findChannel(String channelName) {
    OntologyRepository repository = selectRepository(channelName);
    if (repository == null) {
      return null;
    }
    return repository.findChannel(channelName);
  }

  @Override
  public @Nullable Channel findChannel(Domain sourceDomain, Domain targetDomain) {
    if (sourceDomain.rname() != null) {
      OntologyRepository repository = selectRepository(sourceDomain.rname());
      if (repository == null) {
        return null;
      }
      return repository.findChannel(sourceDomain, targetDomain);
    }
    for (OntologyRepository repository : repositories.values()) {
      Channel channel = repository.findChannel(sourceDomain, targetDomain);
      if (channel != null) {
        return channel;
      }
    }
    return null;
  }

  @Override
  public @Nullable Point findPoint(String pointName) {
    OntologyRepository repository = selectRepository(pointName);
    if (repository == null) {
      return null;
    }
    return repository.findPoint(pointName);
  }

  @Override
  public List<Channel> findProjectionChannels(String domainName) {
    OntologyRepository repository = selectRepository(domainName);
    if (repository == null) {
      return null;
    }
    return repository.findProjectionChannels(domainName);
  }

  @Override
  public List<Reflection> findRelatedReflections(String reflectionName) {
    OntologyRepository repository = selectRepository(reflectionName);
    if (repository == null) {
      return null;
    }
    return repository.findRelatedReflections(reflectionName);
  }

  @Nullable
  private OntologyRepository selectRepository(String reflectionName) {
    String space = SpaceFunctions.getSpaceName(reflectionName);
    return repositories.get(space);
  }
}
