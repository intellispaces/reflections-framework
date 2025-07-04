package tech.intellispaces.reflections.framework.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.core.OntologyRepository;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionChannel;
import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionReference;
import tech.intellispaces.core.ReflectionSpace;
import tech.intellispaces.core.Rid;
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
  public @Nullable Reflection findReflection(ReflectionReference reference) {
    Reflection reflection = null;
    if (reference.rid() != null) {
      reflection = findReflection(reference.rid());
    }
    if (reflection == null && reference.reflectionName() != null) {
      reflection = findReflection(reference.reflectionName());
    }
    return reflection;
  }

  @Override
  public @Nullable ReflectionSpace findSpace(String spaceName) {
    OntologyRepository repository = selectRepository(spaceName);
    if (repository == null) {
      return null;
    }
    return repository.findSpace(spaceName);
  }

  @Override
  public @Nullable ReflectionDomain findDomain(String domainName) {
    OntologyRepository repository = selectRepository(domainName);
    if (repository == null) {
      return null;
    }
    return repository.findDomain(domainName);
  }

  @Override
  public List<ReflectionDomain> findSubdomains(Rid did) {
    for (OntologyRepository repository : repositories.values()) {
      List<ReflectionDomain> subdomains = repository.findSubdomains(did);
      if (subdomains != null) {
        return subdomains;
      }
    }
    return null;
  }

  @Override
  public List<ReflectionDomain> findSubdomains(String domainName) {
    OntologyRepository repository = selectRepository(domainName);
    if (repository == null) {
      return null;
    }
    return repository.findSubdomains(domainName);
  }

  @Override
  public @Nullable ReflectionDomain findBorrowedDomain(String domainName) {
    OntologyRepository repository = selectRepository(domainName);
    if (repository == null) {
      return null;
    }
    return repository.findBorrowedDomain(domainName);
  }

  @Override
  public @Nullable ReflectionChannel findChannel(String channelName) {
    OntologyRepository repository = selectRepository(channelName);
    if (repository == null) {
      return null;
    }
    return repository.findChannel(channelName);
  }

  @Override
  public @Nullable ReflectionChannel findChannel(ReflectionDomain sourceDomain, ReflectionDomain targetDomain) {
    for (OntologyRepository repository : repositories.values()) {
      ReflectionChannel channel = repository.findChannel(sourceDomain, targetDomain);
      if (channel != null) {
        return channel;
      }
    }
    return null;
  }

  @Override
  public List<ReflectionChannel> findDomainChannels(String domainName) {
    OntologyRepository repository = selectRepository(domainName);
    if (repository == null) {
      return null;
    }
    return repository.findDomainChannels(domainName);
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
