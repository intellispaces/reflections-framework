package intellispaces.jaquarius.exception;

import intellispaces.common.base.exception.UnexpectedViolationException;

public class ConfigurationException extends UnexpectedViolationException {

  protected ConfigurationException(String messageTemplate, Object... arguments) {
    super(messageTemplate, arguments);
  }

  protected ConfigurationException(Throwable cause, String messageTemplate, Object... arguments) {
    super(cause, messageTemplate, arguments);
  }

  public static ConfigurationException withMessage(String messageTemplate, Object... messageParams) {
    return new ConfigurationException(null, messageTemplate, messageParams);
  }

  public static ConfigurationException withCauseAndMessage(Throwable cause, String messageTemplate, Object... messageParams) {
    return new ConfigurationException(cause, messageTemplate, messageParams);
  }
}
