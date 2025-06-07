package tech.intellispaces.reflections.framework.settings;

import java.util.List;

/**
 * The ontology reference points.
 */
public interface OntologyReferences {

  List<DomainReference> getDomains();

  List<ChannelReference> getChannels();

  boolean isDomainReferenceSpecified(DomainAssignment assignment);

  DomainReference getDomainByType(DomainAssignment assignment);

  DomainReference getDomainByName(String domainName);

  DomainReference getDomainByClassName(String domainClassName);

  DomainReference getDomainByDelegateClass(String delegateClass);

  boolean isDomainOfDomains(String domainName);

  ChannelReference getChannelByType(ChannelAssignment assignment);
}
