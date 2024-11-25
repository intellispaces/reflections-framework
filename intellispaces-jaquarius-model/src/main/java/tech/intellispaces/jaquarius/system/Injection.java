package tech.intellispaces.jaquarius.system;

public interface Injection {

  InjectionKind kind();

  Object value();
}
