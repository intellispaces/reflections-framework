package tech.intellispacesframework.core.exception;

import tech.intellispacesframework.commons.exception.PossibleViolationException;

public class TraverseException extends PossibleViolationException {

  protected TraverseException(String messageTemplate, Object... arguments) {
    super(messageTemplate, arguments);
  }

  protected TraverseException(Throwable cause, String messageTemplate, Object... arguments) {
    super(cause, messageTemplate, arguments);
  }

  public static TraverseException withMessage(String messageTemplate, Object... messageParams) {
    return new TraverseException(null, messageTemplate, messageParams);
  }

  public static TraverseException withCauseAndMessage(Throwable cause, String messageTemplate, Object... messageParams) {
    return new TraverseException(cause, messageTemplate, messageParams);
  }
}
