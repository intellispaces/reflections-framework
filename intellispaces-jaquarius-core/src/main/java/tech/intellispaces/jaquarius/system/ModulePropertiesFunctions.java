package tech.intellispaces.jaquarius.system;

import tech.intellispaces.general.exception.UnexpectedExceptions;
import tech.intellispaces.general.resource.ResourceFunctions;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public class ModulePropertiesFunctions {

  public static String getProperties(Module module, String filename) {
    FileContents fileContents = CACHE.computeIfAbsent(module, k -> new FileContents());
    String fileContent = fileContents.getContent(filename);
    if (fileContent == null) {
      fileContent = loadPropertiesInternal(module, filename);
      fileContents.addContent(filename, fileContent);
    }
    return fileContent;
  }

  private static String loadPropertiesInternal(Module module, String filename) {
    return readResource(module, filename);
  }

  private static String readResource(Module module, String resourceName) {
    if (resourceName == null) {
      resourceName = "/module.yaml";
    }
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

  private ModulePropertiesFunctions() {}
}
