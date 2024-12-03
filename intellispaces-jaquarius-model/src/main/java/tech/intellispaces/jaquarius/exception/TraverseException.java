package tech.intellispaces.jaquarius.exception;

import tech.intellispaces.general.exception.UnexpectedException;

public class TraverseException extends UnexpectedException {

  public TraverseException(String message) {
    super(message);
  }

  public TraverseException(String message, Throwable cause) {
    super(message, cause);
  }
}
