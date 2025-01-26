package tech.intellispaces.jaquarius.space.domain;

import tech.intellispaces.general.data.Dictionary;
import tech.intellispaces.general.exception.UnexpectedExceptions;

import java.io.IOException;
import java.util.List;

public class BasicDomains {

  public static BasicDomainSet get(List<Dictionary> dictionaries) {
    return BasicDomainFunctions.buildBasicDomainSet(dictionaries);
  }

  public static BasicDomainSet active() {
    if (ACTIVE_BASIC_DOMAINS == null) {
      try {
        ACTIVE_BASIC_DOMAINS = get(
            BasicDomainFunctions.readBasicDomainDictionaries(BasicDomains.class.getClassLoader()));
      } catch (IOException e) {
        throw UnexpectedExceptions.withMessage("Unable to get local primary domain set");
      }
    }
    return ACTIVE_BASIC_DOMAINS;
  }

  public static void setActive(BasicDomainSet basicDomainSet) {
    ACTIVE_BASIC_DOMAINS = basicDomainSet;
  }

  private static BasicDomainSet ACTIVE_BASIC_DOMAINS = null;
}
