package tech.intellispaces.reflectionsframework.engine.description;

public interface UnitMethods {

  static <U> UnitMethodDescriptionBuilder0<U> unitMethod(U unit, String name) {
    return new UnitMethodDescriptionBuilder0<>(unit, name);
  }

  static <U, P> UnitMethodDescriptionBuilder1<U, P> unitMethod(U unit, String name, Class<P> paramClass) {
    return new UnitMethodDescriptionBuilder1<>(unit, name, paramClass);
  }

  static <U, P1, P2> UnitMethodDescriptionBuilder2<U, P1, P2> unitMethod(
      U unit, String name, Class<P1> paramClass1, Class<P2> paramClass2
  ) {
    return new UnitMethodDescriptionBuilder2<>(unit, name, paramClass1, paramClass2);
  }
}
