package tech.intellispaces.reflections.system;

public interface ProjectionInjection extends ProjectionReference, Injection {

  Class<?> unitClass();

  boolean isDefined();
}
