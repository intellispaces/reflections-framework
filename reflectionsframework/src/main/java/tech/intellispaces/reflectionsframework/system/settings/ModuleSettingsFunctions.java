package tech.intellispaces.reflectionsframework.system.settings;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.resource.ResourceFunctions;
import tech.intellispaces.reflectionsframework.system.Module;

public class ModuleSettingsFunctions {

  public static String getSettingsText(Module module, String filename) {
    FileContents fileContents = CACHE.computeIfAbsent(module, k -> new FileContents());
    String fileContent = fileContents.getContent(filename);
    if (fileContent == null) {
      fileContent = readSettings(module, filename);
      fileContents.addContent(filename, fileContent);
    }
    return fileContent;
  }

  private static String readSettings(Module module, String filename) {
    return readSettingsAsResource(module, filename);
  }

  private static String readSettingsAsResource(Module module, String resourceName) {
    final Optional<String> content;
    try {
      content = ResourceFunctions.readResourceAsString(module.getClass(), resourceName);
    } catch (Exception e) {
      throw UnexpectedExceptions.withCauseAndMessage(e, "Failed to read resource '{0}'", resourceName);
    }
    if (content.isEmpty()) {
      throw UnexpectedExceptions.withMessage("Failed to read resource file '{0}'", resourceName);
    }
    return content.get();
  }

  private static final Map<Module, FileContents> CACHE = new WeakHashMap<>();

  private static final class FileContents {
    private final Map<String, String> files = new HashMap<>();

    public String getContent(String filename) {
      return files.get(filename);
    }

    public void addContent(String filename, String fileContent) {
      files.put(filename, fileContent);
    }
  }

  private ModuleSettingsFunctions() {}
}
