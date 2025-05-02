package tech.intellispaces.reflectionsj.system;

public interface ProjectionInjection extends ProjectionReference, Injection {

  Class<?> unitClass();

  boolean isDefined();
}
