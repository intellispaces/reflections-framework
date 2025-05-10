package tech.intellispaces.reflections.framework.exception;

import tech.intellispaces.commons.exception.UnexpectedException;

public class JaquariusException extends UnexpectedException {

  public JaquariusException(String message) {
    super(message);
  }

  public JaquariusException(String message, Throwable cause) {
    super(message, cause);
  }
}
