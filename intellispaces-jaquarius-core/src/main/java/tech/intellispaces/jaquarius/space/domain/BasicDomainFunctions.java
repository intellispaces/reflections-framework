package tech.intellispaces.jaquarius.space.domain;

import tech.intellispaces.commons.base.collection.CollectionFunctions;
import tech.intellispaces.commons.base.data.Dictionaries;
import tech.intellispaces.commons.base.data.Dictionary;
import tech.intellispaces.commons.base.exception.UnexpectedExceptions;
import tech.intellispaces.commons.base.resource.ResourceFunctions;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class BasicDomainFunctions {

  public static Dictionary readBasicDomainDictionary(String baseDirectory) throws IOException {
    var path = Paths.get(baseDirectory, "src/main/resources/META-INF/jaquarius/basic_domain.properties");
    String content = Files.readString(path, StandardCharsets.UTF_8);
    return Dictionaries.ofProperties(content);
  }

  public static List<Dictionary> readBasicDomainDictionaries(ClassLoader classLoader) throws IOException {
    Enumeration<URL> enumeration = classLoader.getResources(
        "META-INF/jaquarius/basic_domain.properties");
    List<URL> urls = CollectionFunctions.toList(enumeration);
    return CollectionFunctions.mapEach(urls, url -> Dictionaries.ofProperties(
        ResourceFunctions.readResourceAsString(url)));
  }

  public static BasicDomainSet buildBasicDomainSet(List<Dictionary> dictionaries) {
    var basicDomains = new ArrayList<BasicDomain>();
    dictionaries.forEach(dictionary ->
        dictionary.propertyNames().forEach(domainName -> {
              BasicDomainPurpose purpose = parseBasicDomainPurpose(dictionary.stringValue(domainName));
              BasicDomain basicDomain = buildBasicDomain(domainName, (BasicDomainPurposes) purpose);
              basicDomains.add(basicDomain);
          }
        )
    );
    return new BasicDomainSetImpl(basicDomains);
  }

  static BasicDomainPurpose parseBasicDomainPurpose(String purpose) {
    return switch (purpose.trim().toLowerCase()) {
      case "point" -> BasicDomainPurposes.Point;
      case "domain" -> BasicDomainPurposes.Domain;
      case "boolean" -> BasicDomainPurposes.Boolean;
      case "string" -> BasicDomainPurposes.String;
      case "number" -> BasicDomainPurposes.Number;
      case "byte" -> BasicDomainPurposes.Byte;
      case "short" -> BasicDomainPurposes.Short;
      case "integer" -> BasicDomainPurposes.Integer;
      case "long" -> BasicDomainPurposes.Long;
      case "float" -> BasicDomainPurposes.Float;
      case "double" -> BasicDomainPurposes.Double;
      default -> throw UnexpectedExceptions.withMessage(
          "Unsupported property '{0}' in file basic_domain.properties", purpose);
    };
  }

  static BasicDomain buildBasicDomain(String domainName, BasicDomainPurposes purpose) {
    return new BasicDomainImpl(domainName, purpose);
  }

  private BasicDomainFunctions() {}
}
