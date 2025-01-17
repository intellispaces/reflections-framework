package tech.intellispaces.jaquarius.space.domain;

import tech.intellispaces.general.data.Dictionary;
import tech.intellispaces.general.exception.UnexpectedExceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrimaryDomainFunctions {

  public static PrimaryDomainSet buildPrimaryDomains(List<Dictionary> dictionaries) {
    var map = new HashMap<PrimaryDomainType, List<PrimaryDomain>>();
    dictionaries.forEach(dictionary ->
        dictionary.propertyNames().forEach(property -> map.compute(
          getPrimaryDomainType(property), (k, v) -> {
            PrimaryDomain primaryDomain = buildPrimaryDomain((PrimaryDomainTypes) k, dictionary.stringValue(property));
            List<PrimaryDomain> list = (v == null) ? new ArrayList<>() : v;
            list.add(primaryDomain);
            return list;
          }))
    );
    return new MapBasedPrimaryDomainSet(map);
  }

  static PrimaryDomainType getPrimaryDomainType(String propertyName) {
    return switch (propertyName) {
      case "domain" -> PrimaryDomainTypes.Domain;
      case "string" -> PrimaryDomainTypes.String;
      case "number" -> PrimaryDomainTypes.Number;
      case "integer" -> PrimaryDomainTypes.Integer;
      default -> throw UnexpectedExceptions.withMessage(
          "Unsupported property '{0}' in file domain.properties", propertyName);
    };
  }

  static PrimaryDomain buildPrimaryDomain(PrimaryDomainTypes type, String domainName) {
    return new PrimaryDomainImpl(type, domainName);
  }

  private PrimaryDomainFunctions() {}
}
