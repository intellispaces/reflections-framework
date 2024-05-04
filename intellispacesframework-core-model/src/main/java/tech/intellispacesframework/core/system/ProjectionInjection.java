package tech.intellispacesframework.core.system;

public interface ProjectionInjection extends Injection {

  String name();

  Class<?> unitClass();

  Class<?> targetClass();
}
