package tech.intellispaces.reflections.framework.settings;

import java.util.List;

/**
 * The ontology reference description.
 */
public interface OntologyReference {

  List<DomainReference> getDomains();

  List<ChannelReference> getChannels();

  boolean isDomainReferenceSpecified(DomainType domainType);

  DomainReference getDomainByType(DomainType domainType);

  DomainReference getDomainByName(String domainName);

  DomainReference getDomainByClassName(String domainClassName);

  DomainReference getDomainByDelegateClass(String delegateClass);

  boolean isDomainOfDomains(String domainName);

  ChannelReference getChannelByType(ChannelType channelType);
}
