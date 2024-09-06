package intellispaces.framework.core.exception;

public class CyclicDependencyException extends ConfigurationException {

  protected CyclicDependencyException(String messageTemplate, Object... arguments) {
    super(messageTemplate, arguments);
  }

  protected CyclicDependencyException(Throwable cause, String messageTemplate, Object... arguments) {
    super(cause, messageTemplate, arguments);
  }

  public static CyclicDependencyException withMessage(String messageTemplate, Object... messageParams) {
    return new CyclicDependencyException(null, messageTemplate, messageParams);
  }

  public static CyclicDependencyException withCauseAndMessage(Throwable cause, String messageTemplate, Object... messageParams) {
    return new CyclicDependencyException(cause, messageTemplate, messageParams);
  }
}
