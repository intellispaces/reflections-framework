package tech.intellispaces.jaquarius.settings;

import tech.intellispaces.commons.exception.UnexpectedExceptions;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class JaquariusSettingsImpl implements JaquariusSettings {
  private final Map<KeyDomainPurpose, KeyDomain> keyDomainIndexByPurpose;
  private final Map<String, KeyDomain> keyDomainIndexByDomainName;
  private final Map<String, KeyDomain> keyDomainIndexByDelegateClassName;
  private final Map<KeyChannelPurpose, KeyChannel> keyChannelIndexByPurpose;

  JaquariusSettingsImpl(
      List<KeyDomain> keyDomains, List<KeyChannel> keyChannels
  ) {
    this.keyDomainIndexByPurpose = keyDomains.stream().collect(Collectors.toMap(KeyDomain::purpose, Function.identity()));
    this.keyDomainIndexByDomainName = keyDomains.stream().collect(Collectors.toMap(KeyDomain::domainName, Function.identity())
    );
    this.keyDomainIndexByDelegateClassName = keyDomains.stream()
        .filter(d -> d.delegateClassName() != null)
        .collect(Collectors.toMap(KeyDomain::delegateClassName, Function.identity())
    );
    this.keyChannelIndexByPurpose = keyChannels.stream().collect(Collectors.toMap(KeyChannel::purpose, Function.identity()));
  }

  @Override
  public KeyDomain getKeyDomainByPurpose(KeyDomainPurpose domainPurpose) {
    KeyDomain keyDomain = keyDomainIndexByPurpose.get(domainPurpose);
    if (keyDomain == null) {
      throw UnexpectedExceptions.withMessage("Unable to get key domain by purpose {0}", domainPurpose.name());
    }
    return keyDomain;
  }

  @Override
  public KeyDomain getKeyDomainByName(String domainName) {
    return keyDomainIndexByDomainName.get(domainName);
  }

  @Override
  public KeyDomain getKeyDomainByDelegateClass(String delegateClassName) {
    return keyDomainIndexByDelegateClassName.get(delegateClassName);
  }

  @Override
  public boolean isDomainOfDomains(String domainName) {
    return getKeyDomainByPurpose(KeyDomainPurposes.Domain).domainName().equals(domainName);
  }

  @Override
  public KeyChannel getKeyChannelByPurpose(KeyChannelPurpose channelPurpose) {
    KeyChannel keyChannel = keyChannelIndexByPurpose.get(channelPurpose);
    if (keyChannel == null) {
      throw UnexpectedExceptions.withMessage("Unable to get key channel by purpose {0}", channelPurpose.name());
    }
    return keyChannel;
  }
}
