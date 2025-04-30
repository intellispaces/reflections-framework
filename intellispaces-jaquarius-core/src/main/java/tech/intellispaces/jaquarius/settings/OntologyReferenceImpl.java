package tech.intellispaces.jaquarius.settings;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import tech.intellispaces.commons.exception.UnexpectedExceptions;

class OntologyReferenceImpl implements OntologyReference {
  private final List<DomainReference> domains;
  private final List<ChannelReference> channels;
  private final Map<DomainType, DomainReference> domainIndexByType;
  private final Map<String, DomainReference> domainIndexByName;
  private final Map<String, DomainReference> domainIndexByDelegateClass;
  private final Map<ChannelType, ChannelReference> channelIndexByType;

  OntologyReferenceImpl(List<DomainReference> domains, List<ChannelReference> channels) {
    this.domains = domains;
    this.channels = channels;
    this.domainIndexByType = domains.stream().collect(Collectors.toMap(DomainReference::type, Function.identity()));
    this.domainIndexByName = domains.stream().collect(Collectors.toMap(DomainReference::domainName, Function.identity())
    );
    this.domainIndexByDelegateClass = domains.stream()
        .filter(d -> d.delegateClassName() != null)
        .collect(Collectors.toMap(DomainReference::delegateClassName, Function.identity())
    );
    this.channelIndexByType = channels.stream().collect(Collectors.toMap(ChannelReference::type, Function.identity()));
  }

  @Override
  public List<DomainReference> getDomains() {
    return domains;
  }

  @Override
  public List<ChannelReference> getChannels() {
    return channels;
  }

  @Override
  public boolean isDomainReferenceSpecified(DomainType domainType) {
    return domainIndexByType.get(domainType) != null;
  }

  @Override
  public DomainReference getDomainByType(DomainType domainType) {
    DomainReference domainReference = domainIndexByType.get(domainType);
    if (domainReference == null) {
      throw UnexpectedExceptions.withMessage("Unable to get domain reference by type {0}",
          domainType.name());
    }
    return domainReference;
  }

  @Override
  public DomainReference getDomainByName(String domainName) {
    return domainIndexByName.get(domainName);
  }

  @Override
  public DomainReference getDomainByDelegateClass(String delegateClass) {
    return domainIndexByDelegateClass.get(delegateClass);
  }

  @Override
  public boolean isDomainOfDomains(String domainName) {
    return getDomainByType(DomainTypes.Domain).domainName().equals(domainName);
  }

  @Override
  public ChannelReference getChannelByType(ChannelType channelType) {
    ChannelReference channelReference = channelIndexByType.get(channelType);
    if (channelReference == null) {
      throw UnexpectedExceptions.withMessage("Unable to get channel reference by type {0}",
          channelType.name());
    }
    return channelReference;
  }
}
