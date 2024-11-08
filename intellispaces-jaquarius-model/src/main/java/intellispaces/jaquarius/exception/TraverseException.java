package intellispaces.jaquarius.exception;

import intellispaces.common.base.exception.UnexpectedException;

public class TraverseException extends UnexpectedException {

  public TraverseException(String message) {
    super(message);
  }

  public TraverseException(String message, Throwable cause) {
    super(message, cause);
  }
}
