package tech.intellispaces.reflections.framework.system;

import java.util.HashMap;
import java.util.Map;

import tech.intellispaces.core.Channel;
import tech.intellispaces.core.Domain;
import tech.intellispaces.core.SpaceFunctions;
import tech.intellispaces.core.repository.SpaceRepository;

public class MultiSpaceRepository implements SpaceRepository {
  private final Map<String, SpaceRepository> repositories = new HashMap<>();

  public void addRepository(String spaceName, SpaceRepository repository) {
    repositories.put(spaceName, repository);
  }

  @Override
  public Domain findDomain(String name) {
    String space = SpaceFunctions.spaceNameOfDomain(name);
    SpaceRepository repository = repositories.get(space);
    if (repository == null) {
      return null;
    }
    return repository.findDomain(name);
  }

  @Override
  public Channel findChannel(String name) {
    String space = SpaceFunctions.spaceNameOfDomain(name);
    SpaceRepository repository = repositories.get(space);
    if (repository == null) {
      return null;
    }
    return repository.findChannel(name);
  }

  @Override
  public Channel findChannel(Domain sourceDomain, Domain targetDomain) {
    if (sourceDomain.name() != null) {
      String space = SpaceFunctions.spaceNameOfDomain(sourceDomain.name());
      SpaceRepository repository = repositories.get(space);
      if (repository == null) {
        return null;
      }
      return repository.findChannel(sourceDomain, targetDomain);
    }
    for (SpaceRepository repository : repositories.values()) {
      Channel channel = repository.findChannel(sourceDomain, targetDomain);
      if (channel != null) {
        return channel;
      }
    }
    return null;
  }
}
