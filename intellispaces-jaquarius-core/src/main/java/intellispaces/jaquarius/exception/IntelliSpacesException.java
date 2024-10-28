package intellispaces.jaquarius.exception;

import intellispaces.common.base.exception.UnexpectedViolationException;

public class IntelliSpacesException extends UnexpectedViolationException {

  protected IntelliSpacesException(String messageTemplate, Object... arguments) {
    super(messageTemplate, arguments);
  }

  protected IntelliSpacesException(Throwable cause, String messageTemplate, Object... arguments) {
    super(cause, messageTemplate, arguments);
  }

  public static IntelliSpacesException withMessage(String messageTemplate, Object... messageParams) {
    return new IntelliSpacesException(null, messageTemplate, messageParams);
  }

  public static IntelliSpacesException withCauseAndMessage(Throwable cause, String messageTemplate, Object... messageParams) {
    return new IntelliSpacesException(cause, messageTemplate, messageParams);
  }
}
