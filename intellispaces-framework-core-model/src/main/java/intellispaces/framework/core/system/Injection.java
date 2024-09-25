package intellispaces.framework.core.system;

public interface Injection {

  InjectionKind kind();

  Object value();
}
