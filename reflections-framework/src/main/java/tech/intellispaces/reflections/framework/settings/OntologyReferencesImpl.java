package tech.intellispaces.reflections.framework.settings;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import tech.intellispaces.commons.exception.UnexpectedExceptions;

class OntologyReferencesImpl implements OntologyReferences {
  private final List<DomainReference> domains;
  private final List<ChannelReference> channels;
  private final Map<DomainAssignment, DomainReference> domainIndexByAssignment;
  private final Map<String, DomainReference> domainIndexByName;
  private final Map<String, DomainReference> domainIndexByClassName;
  private final Map<String, DomainReference> domainIndexByDelegateClassName;
  private final Map<ChannelAssignment, ChannelReference> channelIndexByAssignment;

  OntologyReferencesImpl(List<DomainReference> domains, List<ChannelReference> channels) {
    this.domains = domains;
    this.channels = channels;
    this.domainIndexByAssignment = domains.stream().collect(
        Collectors.toMap(DomainReference::assignment, Function.identity()));
    this.domainIndexByName = domains.stream().collect(
        Collectors.toMap(DomainReference::domainName, Function.identity()));
    this.domainIndexByClassName = domains.stream().collect(
        Collectors.toMap(DomainReference::classCanonicalName, Function.identity()));
    this.domainIndexByDelegateClassName = domains.stream()
        .filter(d -> d.delegateClassName() != null)
        .collect(Collectors.toMap(DomainReference::delegateClassName, Function.identity())
    );
    this.channelIndexByAssignment = channels.stream().collect(
        Collectors.toMap(ChannelReference::assignment, Function.identity()));
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
  public boolean isDomainReferenceSpecified(DomainAssignment assignment) {
    return domainIndexByAssignment.get(assignment) != null;
  }

  @Override
  public DomainReference getDomainByType(DomainAssignment assignment) {
    DomainReference domainReference = domainIndexByAssignment.get(assignment);
    if (domainReference == null) {
      throw UnexpectedExceptions.withMessage("Unable to get domain reference by type {0}",
          assignment.name());
    }
    return domainReference;
  }

  @Override
  public DomainReference getDomainByName(String domainName) {
    return domainIndexByName.get(domainName);
  }

  @Override
  public DomainReference getDomainByClassName(String domainClassName) {
    return domainIndexByClassName.get(domainClassName);
  }

  @Override
  public DomainReference getDomainByDelegateClass(String delegateClass) {
    return domainIndexByDelegateClassName.get(delegateClass);
  }

  @Override
  public boolean isDomainOfDomains(String domainName) {
    return getDomainByType(DomainAssignments.Domain).domainName().equals(domainName);
  }

  @Override
  public ChannelReference getChannelByType(ChannelAssignment assignment) {
    ChannelReference channelReference = channelIndexByAssignment.get(assignment);
    if (channelReference == null) {
      throw UnexpectedExceptions.withMessage("Unable to get channel reference by type {0}",
          assignment.name());
    }
    return channelReference;
  }
}
