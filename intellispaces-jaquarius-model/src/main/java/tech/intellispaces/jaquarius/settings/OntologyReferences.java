package tech.intellispaces.jaquarius.settings;

import java.util.List;

/**
 * The ontology references.
 */
public interface OntologyReferences {

  List<DomainReference> getDomains();

  List<ChannelReference> getChannels();

  DomainReference getDomainByType(DomainType domainType);

  DomainReference getDomainByName(String domainName);

  DomainReference getDomainByDelegateClass(String delegateClass);

  boolean isDomainOfDomains(String domainName);

  ChannelReference getChannelByType(ChannelType channelType);
}
