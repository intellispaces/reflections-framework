package tech.intellispaces.jaquarius.exception;

import tech.intellispaces.commons.base.exception.UnexpectedException;

public class ConfigurationException extends UnexpectedException {

  public ConfigurationException(String message) {
    super(message);
  }

  public ConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }
}
