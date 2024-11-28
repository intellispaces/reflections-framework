package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;
import tech.intellispaces.action.functional.FunctionActions;

import java.util.List;
import java.util.function.Function;

public class ObjectHandleMethodBuilder1<H> {
  private final String methodName;
  private Action guideAction;

  public ObjectHandleMethodBuilder1(Class<H> objectHandleClass, String methodName) {
    this.methodName = methodName;
  }

  public <R> ObjectHandleMethodBuilder1<H> guideFunction(Function<H, R> function) {
    this.guideAction = FunctionActions.ofFunction(function);
    return this;
  }

  public ObjectHandleMethod get() {
    return new ObjectHandleMethodImpl(
        methodName,
        List.of(),
        guideAction,
        guideAction != null ? List.of() : null
    );
  }
}
