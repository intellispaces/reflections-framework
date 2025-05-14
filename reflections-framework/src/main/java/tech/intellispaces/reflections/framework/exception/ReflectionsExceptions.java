package tech.intellispaces.reflections.framework.exception;

import tech.intellispaces.commons.text.StringFunctions;

/**
 * Provider of the exception {@link ReflectionsException}.
 */
public interface ReflectionsExceptions {

  static ReflectionsException withMessage(String message) {
    return new ReflectionsException(message);
  }

  static ReflectionsException withMessage(String template, Object... params) {
    return new ReflectionsException(StringFunctions.resolveTemplate(template, params));
  }

  static ReflectionsException withCauseAndMessage(Throwable cause, String message) {
    return new ReflectionsException(message, cause);
  }

  static ReflectionsException withCauseAndMessage(
      Throwable cause, String template, Object... params
  ) {
    return new ReflectionsException(StringFunctions.resolveTemplate(template, params), cause);
  }
}
