package tech.intellispaces.jaquarius;

import tech.intellispaces.commons.properties.PropertiesSet;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.jaquarius.settings.JaquariusSettings;
import tech.intellispaces.jaquarius.settings.JaquariusSettingsFunctions;

import java.io.IOException;

public class Jaquarius {

  public static JaquariusSettings settings() {
    if (SETTINGS == null) {
      try {
        PropertiesSet properties = JaquariusSettingsFunctions.readSettingsDictionary(Jaquarius.class.getClassLoader());
        SETTINGS = JaquariusSettingsFunctions.buildSettings(properties);
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
