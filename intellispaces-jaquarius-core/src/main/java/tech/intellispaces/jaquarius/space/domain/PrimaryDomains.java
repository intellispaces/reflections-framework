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
import java.util.Enumeration;
import java.util.List;

public class PrimaryDomains {

  public static PrimaryDomainSet current() {
    if (CURRENT_PRIMARY_DOMAINS == null) {
      try {
        CURRENT_PRIMARY_DOMAINS = get(readPrimaryDomainDictionaries((PrimaryDomains.class.getClassLoader())));
      } catch (IOException e) {
        throw UnexpectedExceptions.withMessage("Unable to get local primary domain set");
      }
    }
    return CURRENT_PRIMARY_DOMAINS;
  }

  public static PrimaryDomainSet get(List<Dictionary> dictionaries) {
    return PrimaryDomainFunctions.buildPrimaryDomains(dictionaries);
  }

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

  private static PrimaryDomainSet CURRENT_PRIMARY_DOMAINS = null;

  private PrimaryDomains() {}
}
