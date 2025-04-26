package tech.intellispaces.jaquarius.settings;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import tech.intellispaces.commons.exception.UnexpectedExceptions;

class OntologyDescriptionImpl implements OntologyDescription {
  private final List<DomainDescription> domains;
  private final List<ChannelDescription> channels;
  private final Map<DomainType, DomainDescription> domainIndexByType;
  private final Map<String, DomainDescription> domainIndexByName;
  private final Map<String, DomainDescription> domainIndexByDelegateClass;
  private final Map<ChannelType, ChannelDescription> channelIndexByType;

  OntologyDescriptionImpl(List<DomainDescription> domains, List<ChannelDescription> channels) {
    this.domains = domains;
    this.channels = channels;
    this.domainIndexByType = domains.stream().collect(Collectors.toMap(DomainDescription::type, Function.identity()));
    this.domainIndexByName = domains.stream().collect(Collectors.toMap(DomainDescription::domainName, Function.identity())
    );
    this.domainIndexByDelegateClass = domains.stream()
        .filter(d -> d.delegateClassName() != null)
        .collect(Collectors.toMap(DomainDescription::delegateClassName, Function.identity())
    );
    this.channelIndexByType = channels.stream().collect(Collectors.toMap(ChannelDescription::type, Function.identity()));
  }

  @Override
  public List<DomainDescription> getDomains() {
    return domains;
  }

  @Override
  public List<ChannelDescription> getChannels() {
    return channels;
  }

  @Override
  public DomainDescription getDomainByType(DomainType domainType) {
    DomainDescription domainDescription = domainIndexByType.get(domainType);
    if (domainDescription == null) {
      throw UnexpectedExceptions.withMessage("Unable to get domain by type {0}", domainType.name());
    }
    return domainDescription;
  }

  @Override
  public DomainDescription getDomainByName(String domainName) {
    return domainIndexByName.get(domainName);
  }

  @Override
  public DomainDescription getDomainByDelegateClass(String delegateClass) {
    return domainIndexByDelegateClass.get(delegateClass);
  }

  @Override
  public boolean isDomainOfDomains(String domainName) {
    return getDomainByType(DomainTypes.Domain).domainName().equals(domainName);
  }

  @Override
  public ChannelDescription getChannelByType(ChannelType channelType) {
    ChannelDescription channelDescription = channelIndexByType.get(channelType);
    if (channelDescription == null) {
      throw UnexpectedExceptions.withMessage("Unable to get channel by type {0}", channelType.name());
    }
    return channelDescription;
  }
}
