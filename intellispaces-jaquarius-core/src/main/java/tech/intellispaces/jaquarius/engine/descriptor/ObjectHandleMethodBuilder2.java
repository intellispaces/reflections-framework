package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;
import tech.intellispaces.action.functional.FunctionActions;

import java.util.List;
import java.util.function.BiFunction;

public class ObjectHandleMethodBuilder2<H> {
  private final String methodName;
  private final Class<?> methodParamClass;
  private Action guideAction;
  private Class<?> guideParamClass;

  public ObjectHandleMethodBuilder2(Class<H> objectHandleClass, String methodName, Class<?> methodParamClass) {
    this.methodName = methodName;
    this.methodParamClass = methodParamClass;
  }

  public <P, R> ObjectHandleMethodBuilder2<H> guideFunction(
      BiFunction<H, P, R> function,
      Class<P> guideParamClass
  ) {
    this.guideAction = FunctionActions.ofBiFunction(function);
    this.guideParamClass = guideParamClass;
    return this;
  }

  public ObjectHandleMethod get() {
    return new ObjectHandleMethodImpl(
        methodName,
        List.of(methodParamClass),
        guideAction,
        guideAction != null ? List.of(guideParamClass) : null
    );
  }
}
