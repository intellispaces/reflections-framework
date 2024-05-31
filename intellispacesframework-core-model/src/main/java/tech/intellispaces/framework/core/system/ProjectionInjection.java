package tech.intellispaces.framework.core.system;

public interface ProjectionInjection extends Injection {

  String name();

  Class<?> unitClass();

  Class<?> targetClass();
}
