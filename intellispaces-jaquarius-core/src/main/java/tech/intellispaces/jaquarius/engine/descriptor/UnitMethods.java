package tech.intellispaces.jaquarius.engine.descriptor;

public interface UnitMethods {

  static <U> UnitMethodBuilder0<U> unitMethod(U unit, String name) {
    return new UnitMethodBuilder0<>(unit, name);
  }

  static <U, P> UnitMethodBuilder1<U, P> unitMethod(U unit, String name, Class<P> paramClass) {
    return new UnitMethodBuilder1<>(unit, name, paramClass);
  }

  static <U, P1, P2> UnitMethodBuilder2<U, P1, P2> unitMethod(
      U unit, String name, Class<P1> paramClass1, Class<P2> paramClass2
  ) {
    return new UnitMethodBuilder2<>(unit, name, paramClass1, paramClass2);
  }
}
