package intellispaces.jaquarius.exception;

import intellispaces.common.base.exception.UnexpectedException;

public class IntelliSpacesException extends UnexpectedException {

  public IntelliSpacesException(String message) {
    super(message);
  }

  public IntelliSpacesException(String message, Throwable cause) {
    super(message, cause);
  }
}
