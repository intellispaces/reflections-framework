package tech.intellispaces.jaquarius.settings;

import java.util.List;

/**
 * The ontology description.
 */
public interface OntologyDescription {

  List<DomainDescription> getDomains();

  List<ChannelDescription> getChannels();

  DomainDescription getDomainByType(DomainType domainType);

  DomainDescription getDomainByName(String domainName);

  DomainDescription getDomainByDelegateClass(String delegateClass);

  boolean isDomainOfDomains(String domainName);

  ChannelDescription getChannelByType(ChannelType channelType);
}
