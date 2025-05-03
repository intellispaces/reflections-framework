package tech.intellispaces.reflectionsj.system;

public interface Injection {

  InjectionKind kind();

  Object value();
}
