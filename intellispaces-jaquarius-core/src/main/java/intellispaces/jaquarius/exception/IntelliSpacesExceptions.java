package intellispaces.jaquarius.exception;

import intellispaces.common.base.text.StringFunctions;

/**
 * Provider of the exception {@link IntelliSpacesException}.
 */
public interface IntelliSpacesExceptions {

  static IntelliSpacesException withMessage(String message) {
    return new IntelliSpacesException(message);
  }

  static IntelliSpacesException withMessage(String template, Object... params) {
    return new IntelliSpacesException(StringFunctions.resolveTemplate(template, params));
  }

  static IntelliSpacesException withCauseAndMessage(Throwable cause, String message) {
    return new IntelliSpacesException(message, cause);
  }

  static IntelliSpacesException withCauseAndMessage(
      Throwable cause, String template, Object... params
  ) {
    return new IntelliSpacesException(StringFunctions.resolveTemplate(template, params), cause);
  }
}
