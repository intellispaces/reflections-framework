package tech.intellispaces.reflections.framework.system;

public interface Injection {

  InjectionKind kind();

  Object value();
}
