package tech.intellispaces.jaquarius.settings;

import tech.intellispaces.commons.collection.CollectionFunctions;
import tech.intellispaces.commons.data.Dictionaries;
import tech.intellispaces.commons.data.Dictionary;
import tech.intellispaces.commons.resource.ResourceFunctions;
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

  public static Dictionary readSettingsDictionary(String baseDirectory) throws IOException {
    var path = Paths.get(baseDirectory, "src/main/resources/META-INF/intellispaces/jaquarius.properties");
    String content = Files.readString(path, StandardCharsets.UTF_8);
    return Dictionaries.ofProperties(content);
  }

  public static Dictionary readSettingsDictionary(ClassLoader classLoader) throws IOException {
    Enumeration<URL> enumeration = classLoader.getResources("META-INF/intellispaces/jaquarius.properties");
    List<URL> urls = CollectionFunctions.toList(enumeration);
    List<Dictionary> dictionaries = CollectionFunctions.mapEach(urls, url -> Dictionaries.ofProperties(
        ResourceFunctions.readResourceAsString(url)));
    if (dictionaries.isEmpty()) {
      return null;
    }
    if (dictionaries.size() == 1) {
      return dictionaries.get(0);
    }
    throw ConfigurationExceptions.withMessage("Multiple intellispaces/jaquarius.properties files are found in classpath");
  }

  public static JaquariusSettings buildSettings(Dictionary dictionary) {
    var keyDomains = new ArrayList<KeyDomain>();
    var keyChannels = new ArrayList<KeyChannel>();
    dictionary.propertyNames().forEach(
        propertyName -> parseProperty(dictionary, propertyName, keyDomains, keyChannels)
    );
    return new JaquariusSettingsImpl(keyDomains, keyChannels);
  }

  static void parseProperty(
      Dictionary dictionary,
      String propertyName,
      List<KeyDomain> keyDomains,
      List<KeyChannel> keyChannels
  ) {
    switch (propertyName.trim()) {
      case "point.domain.name":
        keyDomains.add(parseKeyDomain(dictionary, propertyName, KeyDomainPurposes.Point));
        break;
      case "domain.domain.name":
        keyDomains.add(parseKeyDomain(dictionary, propertyName, KeyDomainPurposes.Domain));
        break;
      case "boolean.domain.name":
        keyDomains.add(parseKeyDomain(dictionary, propertyName, KeyDomainPurposes.Boolean));
        break;
      case "string.domain.name":
        keyDomains.add(parseKeyDomain(dictionary, propertyName, KeyDomainPurposes.String));
        break;
      case "number.domain.name":
        keyDomains.add(parseKeyDomain(dictionary, propertyName, KeyDomainPurposes.Number));
        break;
      case "byte.domain.name":
        keyDomains.add(parseKeyDomain(dictionary, propertyName, KeyDomainPurposes.Byte));
        break;
      case "short.domain.name":
        keyDomains.add(parseKeyDomain(dictionary, propertyName, KeyDomainPurposes.Short));
        break;
      case "integer.domain.name":
        keyDomains.add(parseKeyDomain(dictionary, propertyName, KeyDomainPurposes.Integer));
        break;
      case "long.domain.name":
        keyDomains.add(parseKeyDomain(dictionary, propertyName, KeyDomainPurposes.Long));
        break;
      case "float.domain.name":
        keyDomains.add(parseKeyDomain(dictionary, propertyName, KeyDomainPurposes.Float));
        break;
      case "double.domain.name":
        keyDomains.add(parseKeyDomain(dictionary, propertyName, KeyDomainPurposes.Double));
        break;
      case "properties.domain.name":
        keyDomains.add(parseKeyDomain(dictionary, propertyName, KeyDomainPurposes.Properties));
        break;
      case "dataset.domain.name":
        keyDomains.add(parseKeyDomain(dictionary, propertyName, KeyDomainPurposes.Dataset));
        break;
      case "pointToDomain.channel.alias":
        keyChannels.add(parseKeyChannel(dictionary, "pointToDomain.channel", KeyChannelPurposes.PointToDomain));
        break;
      case "yamlStringToProperties.channel.id":
        keyChannels.add(parseKeyChannel(dictionary, "yamlStringToProperties.channel", KeyChannelPurposes.YamlStringToProperties));
        break;
      case "propertiesToValue.channel.id":
        keyChannels.add(parseKeyChannel(dictionary, "propertiesToValue.channel", KeyChannelPurposes.PropertiesToValue));
        break;
      case "propertiesToData.channel.id":
        keyChannels.add(parseKeyChannel(dictionary, "propertiesToData.channel", KeyChannelPurposes.PropertiesToData));
        break;
    };
  }

  static KeyDomain parseKeyDomain(
      Dictionary dictionary, String propertyName, KeyDomainPurposes purpose
  ) {
    String domainName = dictionary.stringValue(propertyName).trim();
    return buildKeyDomain(purpose, domainName);
  }

  static KeyDomain buildKeyDomain(KeyDomainPurposes purpose, String domainName) {
    return new KeyDomainImpl(purpose, domainName);
  }

  static KeyChannel parseKeyChannel(
      Dictionary dictionary, String propertyName, KeyChannelPurposes purpose
  ) {
    String cid = StringFunctions.trim(dictionary.stringValueNullable(propertyName + ".id"));
    String alias = StringFunctions.trim(dictionary.stringValueNullable(propertyName + ".alias"));
    return buildKeyChannel(purpose, cid, alias);
  }

  static KeyChannel buildKeyChannel(KeyChannelPurposes purpose, String cid, String alias) {
    return new KeyChannelImpl(purpose, cid, alias);
  }

  private JaquariusSettingsFunctions() {}
}
