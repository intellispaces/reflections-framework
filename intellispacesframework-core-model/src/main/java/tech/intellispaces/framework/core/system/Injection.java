package tech.intellispaces.framework.core.system;

public interface Injection {

  InjectionType type();

  boolean isDefined();

  Object value();
}
