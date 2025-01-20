package tech.intellispaces.jaquarius.space.domain;

import tech.intellispaces.general.collection.CollectionFunctions;
import tech.intellispaces.general.data.Dictionaries;
import tech.intellispaces.general.data.Dictionary;
import tech.intellispaces.general.exception.UnexpectedExceptions;
import tech.intellispaces.general.resource.ResourceFunctions;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PrimaryDomainFunctions {

  public static Dictionary readPrimaryDomainDictionary(String baseDirectory) throws IOException {
    var path = Paths.get(baseDirectory, "src/main/resources/META-INF/jaquarius/domain.properties");
    String content = Files.readString(path, StandardCharsets.UTF_8);
    return Dictionaries.ofProperties(content);
  }

  public static List<Dictionary> readPrimaryDomainDictionaries(ClassLoader classLoader) throws IOException {
    Enumeration<URL> enumeration = classLoader.getResources(
        "META-INF/jaquarius/domain.properties");
    List<URL> urls = CollectionFunctions.toList(enumeration);
    return CollectionFunctions.mapEach(urls, url -> Dictionaries.ofProperties(
        ResourceFunctions.readResourceAsString(url)));
  }

  public static PrimaryDomainSet buildPrimaryDomains(List<Dictionary> dictionaries) {
    var primaryDomains = new ArrayList<PrimaryDomain>();
    dictionaries.forEach(dictionary ->
        dictionary.propertyNames().forEach(property -> {
              PrimaryDomainType type = getPrimaryDomainType(property);
              PrimaryDomain primaryDomain = buildPrimaryDomain((PrimaryDomainTypes) type, dictionary.stringValue(property));
              primaryDomains.add(primaryDomain);
          }
        )
    );
    return new PrimaryDomainSetImpl(primaryDomains);
  }

  static PrimaryDomainType getPrimaryDomainType(String propertyName) {
    return switch (propertyName) {
      case "domain" -> PrimaryDomainTypes.Domain;
      case "string" -> PrimaryDomainTypes.String;
      case "number" -> PrimaryDomainTypes.Number;
      case "integer8" -> PrimaryDomainTypes.Integer8;
      case "integer16" -> PrimaryDomainTypes.Integer16;
      case "integer32" -> PrimaryDomainTypes.Integer32;
      case "integer64" -> PrimaryDomainTypes.Integer64;
      case "float32" -> PrimaryDomainTypes.Float32;
      case "float64" -> PrimaryDomainTypes.Float64;
      default -> throw UnexpectedExceptions.withMessage(
          "Unsupported property '{0}' in file domain.properties", propertyName);
    };
  }

  static PrimaryDomain buildPrimaryDomain(PrimaryDomainTypes type, String domainName) {
    return new PrimaryDomainImpl(type, domainName);
  }

  private PrimaryDomainFunctions() {}
}
