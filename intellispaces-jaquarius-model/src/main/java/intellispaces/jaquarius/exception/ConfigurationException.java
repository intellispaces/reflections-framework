package intellispaces.jaquarius.exception;

import intellispaces.common.base.exception.UnexpectedException;

public class ConfigurationException extends UnexpectedException {

  public ConfigurationException(String message) {
    super(message);
  }

  public ConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }
}
