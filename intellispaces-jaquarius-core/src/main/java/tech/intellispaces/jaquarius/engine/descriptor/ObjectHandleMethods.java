package tech.intellispaces.jaquarius.engine.descriptor;

public interface ObjectHandleMethods {

  static <H> ObjectHandleMethodBuilder1<H> objectHandleMethod(
      Class<H> objectHandleClass,
      String methodName
  ) {
    return new ObjectHandleMethodBuilder1<>(objectHandleClass, methodName);
  }

  static <H> ObjectHandleMethodBuilder2<H> objectHandleMethod(
      Class<H> objectHandleClass,
      String methodName,
      Class<?> methodParamClass
  ) {
    return new ObjectHandleMethodBuilder2<>(
        objectHandleClass, methodName, methodParamClass
    );
  }

  static <H> ObjectHandleMethodBuilder3<H> objectHandleMethod(
      Class<H> objectHandleClass,
      String methodName,
      Class<?> methodParamClass1,
      Class<?> methodParamClass2
  ) {
    return new ObjectHandleMethodBuilder3<>(
        objectHandleClass, methodName, methodParamClass1, methodParamClass2
    );
  }

  static <H> ObjectHandleMethodBuilder4<H> objectHandleMethod(
      Class<H> objectHandleClass,
      String methodName,
      Class<?> methodParamClass1,
      Class<?> methodParamClass2,
      Class<?> methodParamClass3
  ) {
    return new ObjectHandleMethodBuilder4<>(
        objectHandleClass, methodName, methodParamClass1, methodParamClass2, methodParamClass3
    );
  }

  static <H> ObjectHandleMethodBuilder5<H> objectHandleMethod(
      Class<H> objectHandleClass,
      String methodName,
      Class<?> methodParamClass1,
      Class<?> methodParamClass2,
      Class<?> methodParamClass3,
      Class<?> methodParamClass4
  ) {
    return new ObjectHandleMethodBuilder5<>(
        objectHandleClass, methodName, methodParamClass1, methodParamClass2, methodParamClass3, methodParamClass4
    );
  }
}
