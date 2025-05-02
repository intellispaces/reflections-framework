package tech.intellispaces.reflectionsj.exception;

import tech.intellispaces.commons.exception.UnexpectedException;

public class JaquariusException extends UnexpectedException {

  public JaquariusException(String message) {
    super(message);
  }

  public JaquariusException(String message, Throwable cause) {
    super(message, cause);
  }
}
