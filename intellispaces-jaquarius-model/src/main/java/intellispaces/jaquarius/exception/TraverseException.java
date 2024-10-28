package intellispaces.jaquarius.exception;

import intellispaces.common.base.exception.UnexpectedViolationException;

public class TraverseException extends UnexpectedViolationException {

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
