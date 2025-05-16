package tech.intellispaces.reflections.framework.system;

public interface UnitMethods {

  static <U> UnitMethodBuilder0<U> unitMethod(
      Class<U> unitClass,
      U unit,
      String name
  ) {
    return new UnitMethodBuilder0<>(unitClass, unit, name);
  }

  static <U, P> UnitMethodBuilder1<U, P> unitMethod(
      Class<U> unitClass,
      U unit,
      String name,
      Class<P> paramClass
  ) {
    return new UnitMethodBuilder1<>(unitClass, unit, name, paramClass);
  }

  static <U, P1, P2> UnitMethodBuilder2<U, P1, P2> unitMethod(
      Class<U> unitClass,
      U unit,
      String name,
      Class<P1> paramClass1,
      Class<P2> paramClass2
  ) {
    return new UnitMethodBuilder2<>(unitClass, unit, name, paramClass1, paramClass2);
  }
}
