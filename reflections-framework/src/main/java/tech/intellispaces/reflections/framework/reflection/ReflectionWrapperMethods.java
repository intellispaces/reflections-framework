package tech.intellispaces.reflections.framework.reflection;

public interface ReflectionWrapperMethods {

  static <W> ReflectionRealizationMethodBuilder1<W> reflectionRealizationMethod(
      Class<W> reflectionWrapperClass,
      String name
  ) {
    return new ReflectionRealizationMethodBuilder1<>(reflectionWrapperClass, name);
  }

  static <W, P> ReflectionRealizationMethodBuilder2<W, P> reflectionRealizationMethod(
      Class<W> reflectionWrapperClass,
      String name,
      Class<P> paramClass
  ) {
    return new ReflectionRealizationMethodBuilder2<>(
        reflectionWrapperClass, name, paramClass
    );
  }

  static <W, P1, P2> ReflectionRealizationMethodBuilder3<W, P1, P2> reflectionRealizationMethod(
      Class<W> reflectionWrapperClass,
      String name,
      Class<P1> paramClass1,
      Class<P2> paramClass2
  ) {
    return new ReflectionRealizationMethodBuilder3<>(
        reflectionWrapperClass, name, paramClass1, paramClass2
    );
  }

  static <W, P1, P2, P3> ReflectionRealizationMethodBuilder4<W, P1, P2, P3> reflectionRealizationMethod(
      Class<W> reflectionWrapperClass,
      String name,
      Class<P1> paramClass1,
      Class<P2> paramClass2,
      Class<P3> paramClass3
  ) {
    return new ReflectionRealizationMethodBuilder4<>(
        reflectionWrapperClass, name, paramClass1, paramClass2, paramClass3
    );
  }

  static <W, P1, P2, P3, P4> ReflectionRealizationMethodBuilder5<W, P1, P2, P3, P4> reflectionRealizationMethod(
      Class<W> reflectionWrapperClass,
      String name,
      Class<P1> paramClass1,
      Class<P2> paramClass2,
      Class<P3> paramClass3,
      Class<P4> paramClass4
  ) {
    return new ReflectionRealizationMethodBuilder5<>(
        reflectionWrapperClass, name, paramClass1, paramClass2, paramClass3, paramClass4
    );
  }
}
