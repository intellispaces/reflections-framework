package tech.intellispaces.jaquarius.exception;

public class CyclicDependencyException extends ConfigurationException {

  public CyclicDependencyException(String message) {
    super(message);
  }

  public CyclicDependencyException(String message, Throwable cause) {
    super(message, cause);
  }
}
