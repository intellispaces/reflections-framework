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

  public static OntologyReference loadOntologyReference(String baseDirectory) throws IOException {
    var path = Paths.get(baseDirectory, "src/main/resources/META-INF/jaquarius/ontology.reference");
    String source = Files.readString(path, StandardCharsets.UTF_8);
    PropertiesSet props = PropertiesSets.parseProperties(source, "");
    return parseOntologyProps(props);
  }

  public static List<OntologyReference> loadOntologyReferences(ClassLoader classLoader) throws IOException {
    Enumeration<URL> enumeration = classLoader.getResources("META-INF/jaquarius/ontology.reference");
    List<URL> urls = CollectionFunctions.toList(enumeration);
    List<PropertiesSet> propsList = CollectionFunctions.mapEach(urls, url -> PropertiesSets.parseProperties(
        ResourceFunctions.readResourceAsString(url), ""));
    return CollectionFunctions.mapEach(propsList, SettingsFunctions::parseOntologyProps);
  }

  public static OntologyReference mergeOntologyReferences(List<OntologyReference> ontologyReferences) {
    Set<DomainType> domainTypes = new HashSet<>();
    Set<ChannelType> channelTypes = new HashSet<>();
    List<DomainReference> domains = new ArrayList<>();
    List<ChannelReference> channels = new ArrayList<>();
    for (OntologyReference ontology : ontologyReferences) {
      for (DomainReference domain : ontology.getDomains()) {
        if (!domainTypes.add(domain.type())) {
          throw UnexpectedExceptions.withMessage("Domain reference {0} is already registered", domain.type().name());
        }
        domains.add(domain);
      }
      for (ChannelReference channel : ontology.getChannels()) {
        if (!channelTypes.add(channel.type())) {
          throw UnexpectedExceptions.withMessage("Channel reference {0} is already registered", channel.type().name());
        }
        channels.add(channel);
      }
    }
    return new OntologyReferenceImpl(domains, channels);
  }

  static OntologyReference parseOntologyProps(PropertiesSet props) {
    var domains = new ArrayList<DomainReference>();
    var channels = new ArrayList<ChannelReference>();
    props.propertiesNames().forEach(
        propertyName -> parseProperty(props, propertyName, domains, channels)
    );
    return new OntologyReferenceImpl(domains, channels);
  }

  static void parseProperty(
      PropertiesSet props,
      String propertyName,
      List<DomainReference> domainReferences,
      List<ChannelReference> channelReferences
  ) {
    switch (propertyName.trim()) {
      case "notion.domainName":
        domainReferences.add(parseDomain(props, propertyName, DomainTypes.Notion));
        break;
      case "domain.domainName":
        domainReferences.add(parseDomain(props, propertyName, DomainTypes.Domain));
        break;
      case "boolean.domainName":
        domainReferences.add(parseDomain(props, propertyName, DomainTypes.Boolean));
        break;
      case "string.domainName":
        domainReferences.add(parseDomain(props, propertyName, DomainTypes.String));
        break;
      case "number.domainName":
        domainReferences.add(parseDomain(props, propertyName, DomainTypes.Number));
        break;
      case "byte.domainName":
        domainReferences.add(parseDomain(props, propertyName, DomainTypes.Byte));
        break;
      case "short.domainName":
        domainReferences.add(parseDomain(props, propertyName, DomainTypes.Short));
        break;
      case "integer.domainName":
        domainReferences.add(parseDomain(props, propertyName, DomainTypes.Integer));
        break;
      case "long.domainName":
        domainReferences.add(parseDomain(props, propertyName, DomainTypes.Long));
        break;
      case "float.domainName":
        domainReferences.add(parseDomain(props, propertyName, DomainTypes.Float));
        break;
      case "double.domainName":
        domainReferences.add(parseDomain(props, propertyName, DomainTypes.Double));
        break;
      case "propertiesSet.domainName":
        domainReferences.add(parseDomain(props, propertyName, DomainTypes.PropertiesSet));
        break;
      case "dataset.domainName":
        domainReferences.add(parseDomain(props, propertyName, DomainTypes.Dataset));
        break;
      case "notionToDomain.channelAlias":
        channelReferences.add(parseChannel(props, "notionToDomain.channel", ChannelTypes.PointToDomain));
        break;
      case "yamlStringToPropertiesSet.channelId":
        channelReferences.add(parseChannel(props, "yamlStringToPropertiesSet.channel", ChannelTypes.YamlStringToPropertiesSet));
        break;
      case "propertiesSetToValue.channelId":
        channelReferences.add(parseChannel(props, "propertiesSetToValue.channel", ChannelTypes.PropertiesSetToValue));
        break;
      case "propertiesSetToData.channelId":
        channelReferences.add(parseChannel(props, "propertiesSetToData.channel", ChannelTypes.PropertiesSetToData));
        break;
    };
  }

  static DomainReference parseDomain(
      PropertiesSet props, String propertyName, DomainTypes type
  ) {
    String domainName = props.traverseToStringOrThrow(propertyName).trim();
    return buildDomain(type, domainName);
  }

  static DomainReference buildDomain(DomainTypes type, String domainName) {
    return new DomainReferenceImpl(type, domainName);
  }

  static ChannelReference parseChannel(
      PropertiesSet props, String propertyName, ChannelTypes type
  ) {
    String cid = StringFunctions.trim(props.traverseToString(propertyName + "Id"));
    String alias = StringFunctions.trim(props.traverseToString(propertyName + "Alias"));
    return buildChannel(type, cid, alias);
  }

  static ChannelReference buildChannel(ChannelTypes purpose, String channelId, String alias) {
    return new ChannelReferenceImpl(purpose, channelId, alias);
  }

  private SettingsFunctions() {}
}
