package intellispaces.jaquarius.exception;

import intellispaces.common.base.text.StringFunctions;

/**
 * Provider of the exception {@link CyclicDependencyException}.
 */
public interface CyclicDependencyExceptions {

  static CyclicDependencyException withMessage(String message) {
    return new CyclicDependencyException(message);
  }

  static CyclicDependencyException withMessage(String template, Object... params) {
    return new CyclicDependencyException(StringFunctions.resolveTemplate(template, params));
  }

  static CyclicDependencyException withCauseAndMessage(Throwable cause, String message) {
    return new CyclicDependencyException(message, cause);
  }

  static CyclicDependencyException withCauseAndMessage(
      Throwable cause, String template, Object... params
  ) {
    return new CyclicDependencyException(StringFunctions.resolveTemplate(template, params), cause);
  }
}
