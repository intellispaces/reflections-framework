package tech.intellispaces.reflections.system;

public interface Injection {

  InjectionKind kind();

  Object value();
}
