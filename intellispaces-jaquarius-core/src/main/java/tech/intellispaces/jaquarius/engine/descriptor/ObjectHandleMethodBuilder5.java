package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;
import tech.intellispaces.action.functional.FunctionActions;
import tech.intellispaces.entity.function.QuintiFunction;

import java.util.List;

public class ObjectHandleMethodBuilder5<H> {
  private final String methodName;
  private final Class<?> methodParamClass1;
  private final Class<?> methodParamClass2;
  private final Class<?> methodParamClass3;
  private final Class<?> methodParamClass4;
  private Action guideAction;
  private Class<?> guideParamClass1;
  private Class<?> guideParamClass2;
  private Class<?> guideParamClass3;
  private Class<?> guideParamClass4;

  public ObjectHandleMethodBuilder5(
      Class<H> objectHandleClass,
      String methodName,
      Class<?> methodParamClass1,
      Class<?> methodParamClass2,
      Class<?> methodParamClass3,
      Class<?> methodParamClass4
  ) {
    this.methodName = methodName;
    this.methodParamClass1 = methodParamClass1;
    this.methodParamClass2 = methodParamClass2;
    this.methodParamClass3 = methodParamClass3;
    this.methodParamClass4 = methodParamClass4;
  }

  public <P1, P2, P3, P4, R> ObjectHandleMethodBuilder5<H> guideFunction(
      QuintiFunction<H, P1, P2, P3, P4, R> function,
      Class<P1> guideParamClass1,
      Class<P2> guideParamClass2,
      Class<P3> guideParamClass3,
      Class<P4> guideParamClass4
  ) {
    this.guideAction = FunctionActions.ofQuintiFunction(function);
    this.guideParamClass1 = guideParamClass1;
    this.guideParamClass2 = guideParamClass2;
    this.guideParamClass3 = guideParamClass3;
    this.guideParamClass4 = guideParamClass4;
    return this;
  }

  public ObjectHandleMethod get() {
    return new ObjectHandleMethodImpl(
        methodName,
        List.of(methodParamClass1, methodParamClass2, methodParamClass3, methodParamClass4),
        guideAction,
        guideAction != null ? List.of(guideParamClass1, guideParamClass2, guideParamClass3, guideParamClass4) : null
    );
  }
}
