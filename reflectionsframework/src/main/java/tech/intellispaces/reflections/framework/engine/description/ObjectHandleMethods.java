package tech.intellispaces.reflections.framework.engine.description;

public interface ObjectHandleMethods {

  static <H> ObjectHandleMethodBuilder1<H> objectHandleMethod(
      Class<H> objectHandleClass,
      String name
  ) {
    return new ObjectHandleMethodBuilder1<>(objectHandleClass, name);
  }

  static <H, P> ObjectHandleMethodBuilder2<H, P> objectHandleMethod(
      Class<H> objectHandleClass,
      String name,
      Class<P> paramClass
  ) {
    return new ObjectHandleMethodBuilder2<>(
        objectHandleClass, name, paramClass
    );
  }

  static <H, P1, P2> ObjectHandleMethodBuilder3<H, P1, P2> objectHandleMethod(
      Class<H> objectHandleClass,
      String name,
      Class<P1> paramClass1,
      Class<P2> paramClass2
  ) {
    return new ObjectHandleMethodBuilder3<>(
        objectHandleClass, name, paramClass1, paramClass2
    );
  }

  static <H, P1, P2, P3> ObjectHandleMethodBuilder4<H, P1, P2, P3> objectHandleMethod(
      Class<H> objectHandleClass,
      String name,
      Class<P1> paramClass1,
      Class<P2> paramClass2,
      Class<P3> paramClass3
  ) {
    return new ObjectHandleMethodBuilder4<>(
        objectHandleClass, name, paramClass1, paramClass2, paramClass3
    );
  }

  static <H, P1, P2, P3, P4> ObjectHandleMethodBuilder5<H, P1, P2, P3, P4> objectHandleMethod(
      Class<H> objectHandleClass,
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
