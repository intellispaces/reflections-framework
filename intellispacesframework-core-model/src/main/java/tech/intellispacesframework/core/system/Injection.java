package tech.intellispacesframework.core.system;

public interface Injection {

  InjectionType type();

  boolean isDefined();

  boolean isLazy();

  Object value();
}
