package tech.intellispaces.jaquarius.exception;

import tech.intellispaces.commons.text.StringFunctions;

/**
 * Provider of the exception {@link JaquariusException}.
 */
public interface JaquariusExceptions {

  static JaquariusException withMessage(String message) {
    return new JaquariusException(message);
  }

  static JaquariusException withMessage(String template, Object... params) {
    return new JaquariusException(StringFunctions.resolveTemplate(template, params));
  }

  static JaquariusException withCauseAndMessage(Throwable cause, String message) {
    return new JaquariusException(message, cause);
  }

  static JaquariusException withCauseAndMessage(
      Throwable cause, String template, Object... params
  ) {
    return new JaquariusException(StringFunctions.resolveTemplate(template, params), cause);
  }
}
