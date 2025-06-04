package tech.intellispaces.reflections.framework.settings;

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
import tech.intellispaces.core.Rids;

public class SettingsFunctions {

  public static OntologyReference loadOntologyReference(String baseDirectory) throws IOException {
    var path = Paths.get(baseDirectory, "src/main/resources/META-INF/reflections/ontology.reference");
    String source = Files.readString(path, StandardCharsets.UTF_8);
    PropertiesSet props = PropertiesSets.parseProperties(source, "");
    return readOntologyDescription(props);
  }

  public static List<OntologyReference> loadOntologyReferences(ClassLoader classLoader) throws IOException {
    Enumeration<URL> enumeration = classLoader.getResources("META-INF/reflections/ontology.reference");
    List<URL> urls = CollectionFunctions.toList(enumeration);
    List<PropertiesSet> propsList = CollectionFunctions.mapEach(urls, url -> PropertiesSets.parseProperties(
        ResourceFunctions.readResourceAsString(url), ""));
    return CollectionFunctions.mapEach(propsList, SettingsFunctions::readOntologyDescription);
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

  static OntologyReference readOntologyDescription(PropertiesSet props) {
    var domains = new ArrayList<DomainReference>();
    var channels = new ArrayList<ChannelReference>();
    readNotionOntologyDescription(props, domains, channels);
    readDomainOntologyDescription(props, domains);
    readBooleanOntologyDescription(props, domains);
    readStringOntologyDescription(props, domains);
    readNumberOntologyDescription(props, domains);
    readByteOntologyDescription(props, domains);
    readShortOntologyDescription(props, domains);
    readIntegerOntologyDescription(props, domains);
    readLongOntologyDescription(props, domains);
    readFloatOntologyDescription(props, domains);
    readDoubleOntologyDescription(props, domains);
    readPropertiesSetOntologyDescription(props, domains, channels);
    readDatasetOntologyDescription(props, domains);
    return new OntologyReferenceImpl(domains, channels);
  }

  static void readNotionOntologyDescription(
      PropertiesSet props, List<DomainReference> domains, List<ChannelReference> channels
  ) {
    String domainName = props.traverseToString("notionDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("notionDomain.class");
      domains.add(new DomainReferenceImpl(DomainTypes.Notion, domainName, className));

      String notionToDomainChannelAlias = props.traverseToString("notionToDomainChannel.alias");
      channels.add(new ChannelReferenceImpl(ChannelTypes.PointToDomain, null, notionToDomainChannelAlias));
    }
  }

  static void readDomainOntologyDescription(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("domainDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("domainDomain.class");
      domains.add(new DomainReferenceImpl(DomainTypes.Domain, domainName, className));
    }
  }

  static void readBooleanOntologyDescription(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("booleanDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("booleanDomain.class");
      domains.add(new DomainReferenceImpl(DomainTypes.Boolean, domainName, className));
    }
  }

  static void readStringOntologyDescription(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("stringDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("stringDomain.class");
      domains.add(new DomainReferenceImpl(DomainTypes.String, domainName, className));
    }
  }

  static void readNumberOntologyDescription(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("numberDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("numberDomain.class");
      domains.add(new DomainReferenceImpl(DomainTypes.Number, domainName, className));
    }
  }

  static void readByteOntologyDescription(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("byteDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("byteDomain.class");
      domains.add(new DomainReferenceImpl(DomainTypes.Byte, domainName, className));
    }
  }

  static void readShortOntologyDescription(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("shortDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("shortDomain.class");
      domains.add(new DomainReferenceImpl(DomainTypes.Short, domainName, className));
    }
  }

  static void readIntegerOntologyDescription(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("integerDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("integerDomain.class");
      domains.add(new DomainReferenceImpl(DomainTypes.Integer, domainName, className));
    }
  }

  static void readLongOntologyDescription(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("longDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("longDomain.class");
      domains.add(new DomainReferenceImpl(DomainTypes.Long, domainName, className));
    }
  }

  static void readFloatOntologyDescription(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("floatDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("floatDomain.class");
      domains.add(new DomainReferenceImpl(DomainTypes.Float, domainName, className));
    }
  }

  static void readDoubleOntologyDescription(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("doubleDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("doubleDomain.class");
      domains.add(new DomainReferenceImpl(DomainTypes.Double, domainName, className));
    }
  }

  static void readPropertiesSetOntologyDescription(
      PropertiesSet props, List<DomainReference> domains, List<ChannelReference> channels
  ) {
    String domainName = props.traverseToString("propertiesSetDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("propertiesSetDomain.class");
      domains.add(new DomainReferenceImpl(DomainTypes.PropertiesSet, domainName, className));

      String propertiesSetToValueCid = props.traverseToString("propertiesSetToValueChannel.rid");
      channels.add(new ChannelReferenceImpl(ChannelTypes.PropertiesSetToValue, Rids.create(propertiesSetToValueCid), null));

      String propertiesSetToDataCid = props.traverseToString("propertiesSetToDataChannel.rid");
      channels.add(new ChannelReferenceImpl(ChannelTypes.PropertiesSetToData, Rids.create(propertiesSetToDataCid), null));

      String yamlStringToPropertiesSetCid = props.traverseToString("yamlStringToPropertiesSetChannel.rid");
      channels.add(new ChannelReferenceImpl(ChannelTypes.YamlStringToPropertiesSet, Rids.create(yamlStringToPropertiesSetCid), null));
    }
  }

  static void readDatasetOntologyDescription(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("datasetDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("datasetDomain.class");
      domains.add(new DomainReferenceImpl(DomainTypes.Dataset, domainName, className));
    }
  }

  private SettingsFunctions() {}
}
