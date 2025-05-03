package tech.intellispaces.reflectionsj.exception;

import tech.intellispaces.commons.exception.UnexpectedException;

public class TraverseException extends UnexpectedException {

  public TraverseException(String message) {
    super(message);
  }

  public TraverseException(String message, Throwable cause) {
    super(message, cause);
  }
}
