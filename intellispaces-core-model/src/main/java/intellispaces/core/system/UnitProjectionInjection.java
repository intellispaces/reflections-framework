package intellispaces.core.system;

public interface UnitProjectionInjection extends ProjectionInjection {

  Class<?> unitClass();

  boolean isDefined();

  Object value();
}
