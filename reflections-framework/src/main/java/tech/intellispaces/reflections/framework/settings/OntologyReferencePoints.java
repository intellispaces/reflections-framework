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
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.core.Rid;
import tech.intellispaces.core.Rids;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;

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
    loadDomainReference(DomainAssignments.Notion, "notionDomain", props, domains);
    if (props.hasValue("notionDomain.name")) {
      String notionToDomainChannelAlias = props.getString("notionToDomainChannel.alias");
      channels.add(new ChannelReferenceImpl(ChannelAssignments.PointToDomain, null, notionToDomainChannelAlias));
    }
  }

  static void loadDomainOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    loadDomainReference(DomainAssignments.Domain, "domainDomain", props, domains);
  }

  static void loadBooleanOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    loadDomainReference(DomainAssignments.Boolean, "booleanDomain", props, domains);
  }

  static void loadStringOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    loadDomainReference(DomainAssignments.String, "stringDomain", props, domains);
  }

  static void loadNumberOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    loadDomainReference(DomainAssignments.Number, "numberDomain", props, domains);
  }

  static void loadByteOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    loadDomainReference(DomainAssignments.Byte, "byteDomain", props, domains);
  }

  static void loadShortOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    loadDomainReference(DomainAssignments.Short, "shortDomain", props, domains);
  }

  static void loadIntegerOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    loadDomainReference(DomainAssignments.Integer, "integerDomain", props, domains);
  }

  static void loadLongOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    loadDomainReference(DomainAssignments.Long, "longDomain", props, domains);
  }

  static void loadFloatOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    loadDomainReference(DomainAssignments.Float, "floatDomain", props, domains);
  }

  static void loadDoubleOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    loadDomainReference(DomainAssignments.Double, "doubleDomain", props, domains);
  }

  static void loadPropertiesSetOntologyReferences(
      PropertiesSet props, List<DomainReference> domains, List<ChannelReference> channels
  ) {
    loadDomainReference(DomainAssignments.PropertiesSet, "propertiesSetDomain", props, domains);
    if (props.hasValue("propertiesSetDomain.name")) {
      String propertiesSetToValueCid = props.getString("propertiesSetToValueChannel.rid");
      channels.add(new ChannelReferenceImpl(ChannelAssignments.PropertiesSetToValue, Rids.create(propertiesSetToValueCid), null));

      String propertiesSetToDataCid = props.getString("propertiesSetToDataChannel.rid");
      channels.add(new ChannelReferenceImpl(ChannelAssignments.PropertiesSetToData, Rids.create(propertiesSetToDataCid), null));

      String yamlStringToPropertiesSetCid = props.getString("yamlStringToPropertiesSetChannel.rid");
      channels.add(new ChannelReferenceImpl(ChannelAssignments.YamlStringToPropertiesSet, Rids.create(yamlStringToPropertiesSetCid), null));
    }
  }

  static void loadDatasetOntologyReferences(PropertiesSet props, List<DomainReference> domains) {
    loadDomainReference(DomainAssignments.Dataset, "datasetDomain", props, domains);
  }

  private static void loadDomainReference(
      DomainAssignments domainAssignment, String propertyName, PropertiesSet props, List<DomainReference> domains
  ) {
    String domainAlias = props.getString(propertyName + ".name");
    if (domainAlias != null) {
      String className = props.getString(propertyName + ".class");
      Rid domainId = ClassFunctions.getClass(className)
          .map(DomainFunctions::getDomainId)
          .orElse(null);
      domains.add(new DomainReferenceImpl(domainAssignment, domainId, domainAlias, className));
    }
  }

  private OntologyReferencePoints() {}
}
