package intellispaces.jaquarius.exception;

import intellispaces.common.base.text.StringFunctions;

/**
 * Provider of the exception {@link TraverseException}.
 */
public interface TraverseExceptions {

  static TraverseException withMessage(String message) {
    return new TraverseException(message);
  }

  static TraverseException withMessage(String template, Object... params) {
    return new TraverseException(StringFunctions.resolveTemplate(template, params));
  }

  static TraverseException withCauseAndMessage(Throwable cause, String message) {
    return new TraverseException(message, cause);
  }

  static TraverseException withCauseAndMessage(
      Throwable cause, String template, Object... params
  ) {
    return new TraverseException(StringFunctions.resolveTemplate(template, params), cause);
  }
}
