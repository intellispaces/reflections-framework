package tech.intellispaces.jaquarius;

import tech.intellispaces.commons.data.Dictionary;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.jaquarius.settings.JaquariusSettings;
import tech.intellispaces.jaquarius.settings.JaquariusSettingsFunctions;

import java.io.IOException;

public class Jaquarius {

  public static JaquariusSettings settings() {
    if (SETTINGS == null) {
      try {
        Dictionary dictionary = JaquariusSettingsFunctions.readSettingsDictionary(Jaquarius.class.getClassLoader());
        SETTINGS = JaquariusSettingsFunctions.buildSettings(dictionary);
      } catch (IOException e) {
        throw UnexpectedExceptions.withCauseAndMessage(e, "Unable to read jaquarius settings");
      }
    }
    return SETTINGS;
  }

  public static void settings(JaquariusSettings settings) {
    SETTINGS = settings;
  }

  private static JaquariusSettings SETTINGS = null;
}
