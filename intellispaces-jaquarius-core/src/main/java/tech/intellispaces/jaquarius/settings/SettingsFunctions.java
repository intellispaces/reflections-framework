package tech.intellispaces.jaquarius.settings;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tech.intellispaces.commons.collection.CollectionFunctions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.properties.PropertiesSet;
import tech.intellispaces.commons.properties.PropertiesSets;
import tech.intellispaces.commons.resource.ResourceFunctions;
import tech.intellispaces.commons.text.StringFunctions;

public class SettingsFunctions {

  public static PropertiesSet loadOntologyDescriptionProps(String baseDirectory) throws IOException {
    var path = Paths.get(baseDirectory, "src/main/resources/META-INF/jaquarius/ontology.description");
    String source = Files.readString(path, StandardCharsets.UTF_8);
    return PropertiesSets.parseProperties(source, "");
  }

  public static List<PropertiesSet> loadOntologyDescriptionProps(ClassLoader classLoader) throws IOException {
    Enumeration<URL> enumeration = classLoader.getResources("META-INF/jaquarius/ontology.description");
    List<URL> urls = CollectionFunctions.toList(enumeration);
    List<PropertiesSet> properties = CollectionFunctions.mapEach(urls, url -> PropertiesSets.parseProperties(
        ResourceFunctions.readResourceAsString(url), ""));
    return properties;
  }

  public static OntologyDescription parseOntologyDescription(PropertiesSet props) {
    var keyDomains = new ArrayList<DomainDescription>();
    var keyChannels = new ArrayList<ChannelDescription>();
    props.propertiesNames().forEach(
        propertyName -> parseProperty(props, propertyName, keyDomains, keyChannels)
    );
    return new OntologyDescriptionImpl(keyDomains, keyChannels);
  }

  public static OntologyDescription mergeOntologyDescriptions(List<OntologyDescription> ontologyDescriptions) {
    Set<DomainType> domainTypes = new HashSet<>();
    Set<ChannelType> channelTypes = new HashSet<>();
    List<DomainDescription> domains = new ArrayList<>();
    List<ChannelDescription> channels = new ArrayList<>();
    for (OntologyDescription ontology : ontologyDescriptions) {
      for (DomainDescription domain : ontology.getDomains()) {
        if (!domainTypes.add(domain.type())) {
          throw UnexpectedExceptions.withMessage("Domain type {0} is already registered", domain.type().name());
        }
        domains.add(domain);
      }
      for (ChannelDescription channel : ontology.getChannels()) {
        if (!channelTypes.add(channel.type())) {
          throw UnexpectedExceptions.withMessage("Channel type {0} is already registered", channel.type().name());
        }
        channels.add(channel);
      }
    }
    return new OntologyDescriptionImpl(domains, channels);
  }

  static void parseProperty(
      PropertiesSet props,
      String propertyName,
      List<DomainDescription> domainDescriptions,
      List<ChannelDescription> channelDescriptions
  ) {
    switch (propertyName.trim()) {
      case "point.domain.name":
        domainDescriptions.add(parseDomain(props, propertyName, DomainTypes.Point));
        break;
      case "domain.domain.name":
        domainDescriptions.add(parseDomain(props, propertyName, DomainTypes.Domain));
        break;
      case "boolean.domain.name":
        domainDescriptions.add(parseDomain(props, propertyName, DomainTypes.Boolean));
        break;
      case "string.domain.name":
        domainDescriptions.add(parseDomain(props, propertyName, DomainTypes.String));
        break;
      case "number.domain.name":
        domainDescriptions.add(parseDomain(props, propertyName, DomainTypes.Number));
        break;
      case "byte.domain.name":
        domainDescriptions.add(parseDomain(props, propertyName, DomainTypes.Byte));
        break;
      case "short.domain.name":
        domainDescriptions.add(parseDomain(props, propertyName, DomainTypes.Short));
        break;
      case "integer.domain.name":
        domainDescriptions.add(parseDomain(props, propertyName, DomainTypes.Integer));
        break;
      case "long.domain.name":
        domainDescriptions.add(parseDomain(props, propertyName, DomainTypes.Long));
        break;
      case "float.domain.name":
        domainDescriptions.add(parseDomain(props, propertyName, DomainTypes.Float));
        break;
      case "double.domain.name":
        domainDescriptions.add(parseDomain(props, propertyName, DomainTypes.Double));
        break;
      case "properties.domain.name":
        domainDescriptions.add(parseDomain(props, propertyName, DomainTypes.Properties));
        break;
      case "dataset.domain.name":
        domainDescriptions.add(parseDomain(props, propertyName, DomainTypes.Dataset));
        break;
      case "pointToDomain.channel.alias":
        channelDescriptions.add(parseChannel(props, "pointToDomain.channel", ChannelTypes.PointToDomain));
        break;
      case "yamlStringToProperties.channel.id":
        channelDescriptions.add(parseChannel(props, "yamlStringToProperties.channel", ChannelTypes.YamlStringToProperties));
        break;
      case "propertiesToValue.channel.id":
        channelDescriptions.add(parseChannel(props, "propertiesToValue.channel", ChannelTypes.PropertiesToValue));
        break;
      case "propertiesToData.channel.id":
        channelDescriptions.add(parseChannel(props, "propertiesToData.channel", ChannelTypes.PropertiesToData));
        break;
    };
  }

  static DomainDescription parseDomain(
      PropertiesSet props, String propertyName, DomainTypes type
  ) {
    String domainName = props.traverseToStringOrThrow(propertyName).trim();
    return buildDomain(type, domainName);
  }

  static DomainDescription buildDomain(DomainTypes type, String domainName) {
    return new DomainDescriptionImpl(type, domainName);
  }

  static ChannelDescription parseChannel(
      PropertiesSet props, String propertyName, ChannelTypes type
  ) {
    String cid = StringFunctions.trim(props.traverseToString(propertyName + ".id"));
    String alias = StringFunctions.trim(props.traverseToString(propertyName + ".alias"));
    return buildChannel(type, cid, alias);
  }

  static ChannelDescription buildChannel(ChannelTypes purpose, String channelId, String alias) {
    return new ChannelDescriptionImpl(purpose, channelId, alias);
  }

  private SettingsFunctions() {}
}
