package tech.intellispaces.reflections.framework.engine.description;

public interface ObjectHandleMethods {

  static <R> ObjectHandleMethodBuilder1<R> objectHandleMethod(
      Class<R> objectHandleClass,
      String name
  ) {
    return new ObjectHandleMethodBuilder1<>(objectHandleClass, name);
  }

  static <R, P> ObjectHandleMethodBuilder2<R, P> objectHandleMethod(
      Class<R> objectHandleClass,
      String name,
      Class<P> paramClass
  ) {
    return new ObjectHandleMethodBuilder2<>(
        objectHandleClass, name, paramClass
    );
  }

  static <R, P1, P2> ObjectHandleMethodBuilder3<R, P1, P2> objectHandleMethod(
      Class<R> objectHandleClass,
      String name,
      Class<P1> paramClass1,
      Class<P2> paramClass2
  ) {
    return new ObjectHandleMethodBuilder3<>(
        objectHandleClass, name, paramClass1, paramClass2
    );
  }

  static <R, P1, P2, P3> ObjectHandleMethodBuilder4<R, P1, P2, P3> objectHandleMethod(
      Class<R> objectHandleClass,
      String name,
      Class<P1> paramClass1,
      Class<P2> paramClass2,
      Class<P3> paramClass3
  ) {
    return new ObjectHandleMethodBuilder4<>(
        objectHandleClass, name, paramClass1, paramClass2, paramClass3
    );
  }

  static <R, P1, P2, P3, P4> ObjectHandleMethodBuilder5<R, P1, P2, P3, P4> objectHandleMethod(
      Class<R> objectHandleClass,
      String name,
      Class<P1> paramClass1,
      Class<P2> paramClass2,
      Class<P3> paramClass3,
      Class<P4> paramClass4
  ) {
    return new ObjectHandleMethodBuilder5<>(
        objectHandleClass, name, paramClass1, paramClass2, paramClass3, paramClass4
    );
  }
}
