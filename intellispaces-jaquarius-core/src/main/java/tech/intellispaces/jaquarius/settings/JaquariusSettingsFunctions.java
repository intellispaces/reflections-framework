package tech.intellispaces.jaquarius.settings;

import tech.intellispaces.commons.collection.CollectionFunctions;
import tech.intellispaces.commons.resource.ResourceFunctions;
import tech.intellispaces.commons.properties.PropertiesSet;
import tech.intellispaces.commons.properties.PropertiesSets;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class JaquariusSettingsFunctions {

  public static PropertiesSet readSettingsDictionary(String baseDirectory) throws IOException {
    var path = Paths.get(baseDirectory, "src/main/resources/META-INF/intellispaces/jaquarius.properties");
    String source = Files.readString(path, StandardCharsets.UTF_8);
    return PropertiesSets.parseProperties(source, "");
  }

  public static PropertiesSet readSettingsDictionary(ClassLoader classLoader) throws IOException {
    Enumeration<URL> enumeration = classLoader.getResources("META-INF/intellispaces/jaquarius.properties");
    List<URL> urls = CollectionFunctions.toList(enumeration);
    List<PropertiesSet> properties = CollectionFunctions.mapEach(urls, url -> PropertiesSets.parseProperties(
        ResourceFunctions.readResourceAsString(url), ""));
    if (properties.isEmpty()) {
      return null;
    }
    if (properties.size() == 1) {
      return properties.get(0);
    }
    throw ConfigurationExceptions.withMessage("Multiple intellispaces/jaquarius.properties files are found in classpath");
  }

  public static JaquariusSettings buildSettings(PropertiesSet properties) {
    var keyDomains = new ArrayList<KeyDomain>();
    var keyChannels = new ArrayList<KeyChannel>();
    properties.propertiesNames().forEach(
        propertyName -> parseProperty(properties, propertyName, keyDomains, keyChannels)
    );
    return new JaquariusSettingsImpl(keyDomains, keyChannels);
  }

  static void parseProperty(
      PropertiesSet properties,
      String propertyName,
      List<KeyDomain> keyDomains,
      List<KeyChannel> keyChannels
  ) {
    switch (propertyName.trim()) {
      case "point.domain.name":
        keyDomains.add(parseKeyDomain(properties, propertyName, KeyDomainPurposes.Point));
        break;
      case "domain.domain.name":
        keyDomains.add(parseKeyDomain(properties, propertyName, KeyDomainPurposes.Domain));
        break;
      case "boolean.domain.name":
        keyDomains.add(parseKeyDomain(properties, propertyName, KeyDomainPurposes.Boolean));
        break;
      case "string.domain.name":
        keyDomains.add(parseKeyDomain(properties, propertyName, KeyDomainPurposes.String));
        break;
      case "number.domain.name":
        keyDomains.add(parseKeyDomain(properties, propertyName, KeyDomainPurposes.Number));
        break;
      case "byte.domain.name":
        keyDomains.add(parseKeyDomain(properties, propertyName, KeyDomainPurposes.Byte));
        break;
      case "short.domain.name":
        keyDomains.add(parseKeyDomain(properties, propertyName, KeyDomainPurposes.Short));
        break;
      case "integer.domain.name":
        keyDomains.add(parseKeyDomain(properties, propertyName, KeyDomainPurposes.Integer));
        break;
      case "long.domain.name":
        keyDomains.add(parseKeyDomain(properties, propertyName, KeyDomainPurposes.Long));
        break;
      case "float.domain.name":
        keyDomains.add(parseKeyDomain(properties, propertyName, KeyDomainPurposes.Float));
        break;
      case "double.domain.name":
        keyDomains.add(parseKeyDomain(properties, propertyName, KeyDomainPurposes.Double));
        break;
      case "properties.domain.name":
        keyDomains.add(parseKeyDomain(properties, propertyName, KeyDomainPurposes.Properties));
        break;
      case "dataset.domain.name":
        keyDomains.add(parseKeyDomain(properties, propertyName, KeyDomainPurposes.Dataset));
        break;
      case "pointToDomain.channel.alias":
        keyChannels.add(parseKeyChannel(properties, "pointToDomain.channel", KeyChannelPurposes.PointToDomain));
        break;
      case "yamlStringToProperties.channel.id":
        keyChannels.add(parseKeyChannel(properties, "yamlStringToProperties.channel", KeyChannelPurposes.YamlStringToProperties));
        break;
      case "propertiesToValue.channel.id":
        keyChannels.add(parseKeyChannel(properties, "propertiesToValue.channel", KeyChannelPurposes.PropertiesToValue));
        break;
      case "propertiesToData.channel.id":
        keyChannels.add(parseKeyChannel(properties, "propertiesToData.channel", KeyChannelPurposes.PropertiesToData));
        break;
    };
  }

  static KeyDomain parseKeyDomain(
      PropertiesSet properties, String propertyName, KeyDomainPurposes purpose
  ) {
    String domainName = properties.traverseToStringOrThrow(propertyName).trim();
    return buildKeyDomain(purpose, domainName);
  }

  static KeyDomain buildKeyDomain(KeyDomainPurposes purpose, String domainName) {
    return new KeyDomainImpl(purpose, domainName);
  }

  static KeyChannel parseKeyChannel(
      PropertiesSet properties, String propertyName, KeyChannelPurposes purpose
  ) {
    String cid = StringFunctions.trim(properties.traverseToString(propertyName + ".id"));
    String alias = StringFunctions.trim(properties.traverseToString(propertyName + ".alias"));
    return buildKeyChannel(purpose, cid, alias);
  }

  static KeyChannel buildKeyChannel(KeyChannelPurposes purpose, String channelId, String alias) {
    return new KeyChannelImpl(purpose, channelId, alias);
  }

  private JaquariusSettingsFunctions() {}
}
