package tech.intellispaces.reflections.framework.reflection;

public interface ReflectionWrapperMethods {

  static <W> ReflectionImplementationMethodBuilder1<W> reflectionImplementationMethod(
      Class<W> reflectionWrapperClass,
      String name
  ) {
    return new ReflectionImplementationMethodBuilder1<>(reflectionWrapperClass, name);
  }

  static <W, P> ReflectionImplementationMethodBuilder2<W, P> reflectionImplementationMethod(
      Class<W> reflectionWrapperClass,
      String name,
      Class<P> paramClass
  ) {
    return new ReflectionImplementationMethodBuilder2<>(
        reflectionWrapperClass, name, paramClass
    );
  }

  static <W, P1, P2> ReflectionImplementationMethodBuilder3<W, P1, P2> reflectionImplementationMethod(
      Class<W> reflectionWrapperClass,
      String name,
      Class<P1> paramClass1,
      Class<P2> paramClass2
  ) {
    return new ReflectionImplementationMethodBuilder3<>(
        reflectionWrapperClass, name, paramClass1, paramClass2
    );
  }

  static <W, P1, P2, P3> ReflectionImplementationMethodBuilder4<W, P1, P2, P3> reflectionImplementationMethod(
      Class<W> reflectionWrapperClass,
      String name,
      Class<P1> paramClass1,
      Class<P2> paramClass2,
      Class<P3> paramClass3
  ) {
    return new ReflectionImplementationMethodBuilder4<>(
        reflectionWrapperClass, name, paramClass1, paramClass2, paramClass3
    );
  }

  static <W, P1, P2, P3, P4> ReflectionImplementationMethodBuilder5<W, P1, P2, P3, P4> reflectionImplementationMethod(
      Class<W> reflectionWrapperClass,
      String name,
      Class<P1> paramClass1,
      Class<P2> paramClass2,
      Class<P3> paramClass3,
      Class<P4> paramClass4
  ) {
    return new ReflectionImplementationMethodBuilder5<>(
        reflectionWrapperClass, name, paramClass1, paramClass2, paramClass3, paramClass4
    );
  }
}
