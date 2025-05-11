package tech.intellispaces.reflections.framework.exception;

import tech.intellispaces.commons.exception.UnexpectedException;

public class ConfigurationException extends UnexpectedException {

  public ConfigurationException(String message) {
    super(message);
  }

  public ConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }
}
