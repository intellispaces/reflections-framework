package tech.intellispaces.reflections.framework.exception;

import tech.intellispaces.commons.exception.UnexpectedException;

public class ReflectionsException extends UnexpectedException {

  public ReflectionsException(String message) {
    super(message);
  }

  public ReflectionsException(String message, Throwable cause) {
    super(message, cause);
  }
}
