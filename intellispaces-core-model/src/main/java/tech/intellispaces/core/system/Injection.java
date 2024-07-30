package tech.intellispaces.core.system;

public interface Injection {

  InjectionType type();

  boolean isDefined();

  Object value();
}
