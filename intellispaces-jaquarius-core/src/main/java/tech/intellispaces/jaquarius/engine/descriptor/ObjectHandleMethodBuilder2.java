package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;
import tech.intellispaces.action.functional.FunctionActions;
import tech.intellispaces.jaquarius.traverse.TraverseType;

import java.util.List;
import java.util.function.BiFunction;

public class ObjectHandleMethodBuilder2<H, P> {
  private final String name;
  private final Class<P> paramClass;

  private Action action;
  private String purpose;
  private int ordinal;
  private Class<?> channelClass;
  private TraverseType traverseType;

  public ObjectHandleMethodBuilder2(Class<H> objectHandleClass, String name, Class<P> paramClass) {
    this.name = name;
    this.paramClass = paramClass;
  }

  public ObjectHandleMethodBuilder2<H, P> purpose(String purpose) {
    this.purpose = purpose;
    return this;
  }

  public ObjectHandleMethodBuilder2<H, P> ordinal(int ordinal) {
    this.ordinal = ordinal;
    return this;
  }

  public ObjectHandleMethodBuilder2<H, P> channelClass(Class<?> channelClass) {
    this.channelClass = channelClass;
    return this;
  }

  public ObjectHandleMethodBuilder2<H, P> traverseType(TraverseType traverseType) {
    this.traverseType = traverseType;
    return this;
  }

  public <R> ObjectHandleMethodBuilder2<H, P> function(BiFunction<H, P, R> function) {
    this.action = FunctionActions.ofBiFunction(function);
    return this;
  }

  public ObjectHandleMethod get() {
    return new ObjectHandleMethodImpl(
        name,
        List.of(paramClass),
        purpose,
        ordinal,
        action,
        channelClass,
        traverseType
    );
  }
}
