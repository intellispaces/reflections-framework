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

public class OntologyReferencePoints {

  public static OntologyReferences load(String baseDirectory) throws IOException {
    var path = Paths.get(baseDirectory, "src/main/resources/META-INF/reflections/ontology.references");
    String source = Files.readString(path, StandardCharsets.UTF_8);
    PropertiesSet props = PropertiesSets.parseProperties(source, "");
    return load(props);
  }

  public static List<OntologyReferences> load(ClassLoader classLoader) throws IOException {
    Enumeration<URL> enumeration = classLoader.getResources("META-INF/reflections/ontology.references");
    List<URL> urls = CollectionFunctions.toList(enumeration);
    List<PropertiesSet> propsList = CollectionFunctions.mapEach(urls, url -> PropertiesSets.parseProperties(
        ResourceFunctions.readResourceAsString(url), ""));
    return CollectionFunctions.mapEach(propsList, OntologyReferencePoints::load);
  }

  public static OntologyReferences merge(List<OntologyReferences> ontologyReferences) {
    Set<DomainAssignment> domainAssignments = new HashSet<>();
    Set<ChannelAssignment> channelAssignments = new HashSet<>();
    List<DomainReference> domains = new ArrayList<>();
    List<ChannelReference> channels = new ArrayList<>();
    for (OntologyReferences ontology : ontologyReferences) {
      for (DomainReference domain : ontology.getDomains()) {
        if (!domainAssignments.add(domain.assignment())) {
          throw UnexpectedExceptions.withMessage("ReflectionDomain reference {0} is already registered", domain.assignment().name());
        }
        domains.add(domain);
      }
      for (ChannelReference channel : ontology.getChannels()) {
        if (!channelAssignments.add(channel.assignment())) {
          throw UnexpectedExceptions.withMessage("ReflectionChannel reference {0} is already registered", channel.assignment().name());
        }
        channels.add(channel);
      }
    }
    return new OntologyReferencesImpl(domains, channels);
  }

  static OntologyReferences load(PropertiesSet props) {
    var domains = new ArrayList<DomainReference>();
    var channels = new ArrayList<ChannelReference>();
    loadNotionOntologyReferences(props, domains, channels);
    loadDomainOntologyReferences(props, domains);
    loadBooleanOntologyReferences(props, domains);
    loadStringOntologyReferences(props, domains);
    loadNumberOntologyReferences(props, domains);
    loadByteOntologyReferences(props, domains);
    loadShortOntologyReferences(props, domains);
    loadIntegerOntologyReferences(props, domains);
    loadLongOntologyReferences(props, domains);
    loadFloatOntologyReferences(props, domains);
    loadDoubleOntologyReferences(props, domains);
    loadPropertiesSetOntologyReferences(props, domains, channels);
    loadDatasetOntologyReferences(props, domains);
    return new OntologyReferencesImpl(domains, channels);
  }

  static void loadNotionOntologyReferences(
      PropertiesSet props, List<DomainReference> domains, List<ChannelReference> channels
  ) {
    String domainName = props.traverseToString("notionDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("notionDomain.class");
      domains.add(new DomainReferenceImpl(DomainAssignments.Notion, domainName, className));

      String notionToDomainChannelAlias = props.traverseToString("notionToDomainChannel.alias");
      channels.add(new ChannelReferenceImpl(ChannelAssignments.PointToDomain, null, notionToDomainChannelAlias));
    }
  }

  static void loadDomainOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("domainDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("domainDomain.class");
      domains.add(new DomainReferenceImpl(DomainAssignments.Domain, domainName, className));
    }
  }

  static void loadBooleanOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("booleanDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("booleanDomain.class");
      domains.add(new DomainReferenceImpl(DomainAssignments.Boolean, domainName, className));
    }
  }

  static void loadStringOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("stringDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("stringDomain.class");
      domains.add(new DomainReferenceImpl(DomainAssignments.String, domainName, className));
    }
  }

  static void loadNumberOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("numberDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("numberDomain.class");
      domains.add(new DomainReferenceImpl(DomainAssignments.Number, domainName, className));
    }
  }

  static void loadByteOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("byteDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("byteDomain.class");
      domains.add(new DomainReferenceImpl(DomainAssignments.Byte, domainName, className));
    }
  }

  static void loadShortOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("shortDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("shortDomain.class");
      domains.add(new DomainReferenceImpl(DomainAssignments.Short, domainName, className));
    }
  }

  static void loadIntegerOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("integerDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("integerDomain.class");
      domains.add(new DomainReferenceImpl(DomainAssignments.Integer, domainName, className));
    }
  }

  static void loadLongOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("longDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("longDomain.class");
      domains.add(new DomainReferenceImpl(DomainAssignments.Long, domainName, className));
    }
  }

  static void loadFloatOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("floatDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("floatDomain.class");
      domains.add(new DomainReferenceImpl(DomainAssignments.Float, domainName, className));
    }
  }

  static void loadDoubleOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("doubleDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("doubleDomain.class");
      domains.add(new DomainReferenceImpl(DomainAssignments.Double, domainName, className));
    }
  }

  static void loadPropertiesSetOntologyReferences(
      PropertiesSet props, List<DomainReference> domains, List<ChannelReference> channels
  ) {
    String domainName = props.traverseToString("propertiesSetDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("propertiesSetDomain.class");
      domains.add(new DomainReferenceImpl(DomainAssignments.PropertiesSet, domainName, className));

      String propertiesSetToValueCid = props.traverseToString("propertiesSetToValueChannel.rid");
      channels.add(new ChannelReferenceImpl(ChannelAssignments.PropertiesSetToValue, Rids.create(propertiesSetToValueCid), null));

      String propertiesSetToDataCid = props.traverseToString("propertiesSetToDataChannel.rid");
      channels.add(new ChannelReferenceImpl(ChannelAssignments.PropertiesSetToData, Rids.create(propertiesSetToDataCid), null));

      String yamlStringToPropertiesSetCid = props.traverseToString("yamlStringToPropertiesSetChannel.rid");
      channels.add(new ChannelReferenceImpl(ChannelAssignments.YamlStringToPropertiesSet, Rids.create(yamlStringToPropertiesSetCid), null));
    }
  }

  static void loadDatasetOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    String domainName = props.traverseToString("datasetDomain.name");
    if (domainName != null) {
      String className = props.traverseToString("datasetDomain.class");
      domains.add(new DomainReferenceImpl(DomainAssignments.Dataset, domainName, className));
    }
  }

  private OntologyReferencePoints() {}
}
