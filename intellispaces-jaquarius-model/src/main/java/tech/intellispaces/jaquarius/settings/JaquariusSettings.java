package tech.intellispaces.jaquarius.settings;

/**
 * The Jaquarius framework settings.
 */
public interface JaquariusSettings {

  KeyDomain getKeyDomainByPurpose(KeyDomainPurpose domainPurpose);

  KeyDomain getKeyDomainByName(String domainName);

  KeyDomain getKeyDomainByDelegateClass(String delegateClassName);

  boolean isDomainOfDomains(String domainName);

  KeyChannel getKeyChannelByPurpose(KeyChannelPurpose channelPurpose);
}
