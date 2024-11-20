package intellispaces.jaquarius.exception;

import tech.intellispaces.entity.exception.UnexpectedException;

public class JaquariusException extends UnexpectedException {

  public JaquariusException(String message) {
    super(message);
  }

  public JaquariusException(String message, Throwable cause) {
    super(message, cause);
  }
}
