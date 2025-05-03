package tech.intellispaces.reflections.exception;

import tech.intellispaces.commons.text.StringFunctions;

/**
 * Provider of the exception {@link ConfigurationException}.
 */
public interface ConfigurationExceptions {

  static ConfigurationException withMessage(String message) {
    return new ConfigurationException(message);
  }

  static ConfigurationException withMessage(String template, Object... params) {
    return new ConfigurationException(StringFunctions.resolveTemplate(template, params));
  }

  static ConfigurationException withCauseAndMessage(Throwable cause, String message) {
    return new ConfigurationException(message, cause);
  }

  static ConfigurationException withCauseAndMessage(
      Throwable cause, String template, Object... params
  ) {
    return new ConfigurationException(StringFunctions.resolveTemplate(template, params), cause);
  }
}
