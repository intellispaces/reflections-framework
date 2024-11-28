package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;
import tech.intellispaces.action.functional.FunctionActions;
import tech.intellispaces.entity.function.TriFunction;

import java.util.List;

public class ObjectHandleMethodBuilder3<H> {
  private final String methodName;
  private final Class<?> methodParamClass1;
  private final Class<?> methodParamClass2;
  private Action guideAction;
  private Class<?> guideParamClass1;
  private Class<?> guideParamClass2;

  public ObjectHandleMethodBuilder3(
      Class<H> objectHandleClass, String methodName, Class<?> methodParamClass1, Class<?> methodParamClass2
  ) {
    this.methodName = methodName;
    this.methodParamClass1 = methodParamClass1;
    this.methodParamClass2 = methodParamClass2;
  }

  public <P1, P2, R> ObjectHandleMethodBuilder3<H> guideFunction(
      TriFunction<H, P1, P2, R> function,
      Class<P1> guideParamClass1,
      Class<P2> guideParamClass2
  ) {
    this.guideAction = FunctionActions.ofTriFunction(function);
    this.guideParamClass1 = guideParamClass1;
    this.guideParamClass2 = guideParamClass2;
    return this;
  }

  public ObjectHandleMethod get() {
    return new ObjectHandleMethodImpl(
        methodName,
        List.of(methodParamClass1, methodParamClass2),
        guideAction,
        guideAction != null ? List.of(guideParamClass1, guideParamClass2) : null
    );
  }
}
