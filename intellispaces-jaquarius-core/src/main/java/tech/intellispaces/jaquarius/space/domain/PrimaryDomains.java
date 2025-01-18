package tech.intellispaces.jaquarius.space.domain;

import tech.intellispaces.general.data.Dictionary;
import tech.intellispaces.general.exception.UnexpectedExceptions;

import java.io.IOException;
import java.util.List;

public class PrimaryDomains {

  public static PrimaryDomainSet get(List<Dictionary> dictionaries) {
    return PrimaryDomainFunctions.buildPrimaryDomains(dictionaries);
  }

  public static PrimaryDomainSet current() {
    if (CURRENT_PRIMARY_DOMAINS == null) {
      try {
        CURRENT_PRIMARY_DOMAINS = get(
            PrimaryDomainFunctions.readPrimaryDomainDictionaries((PrimaryDomains.class.getClassLoader()))
        );
      } catch (IOException e) {
        throw UnexpectedExceptions.withMessage("Unable to get local primary domain set");
      }
    }
    return CURRENT_PRIMARY_DOMAINS;
  }

  public static void current(PrimaryDomainSet primaryDomainSet) {
    CURRENT_PRIMARY_DOMAINS = primaryDomainSet;
  }

  private static PrimaryDomainSet CURRENT_PRIMARY_DOMAINS = null;
}
